/*
 * Copyright 2014 Higher Frequency Trading
 *
 * http://www.higherfrequencytrading.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package compilation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("StaticNonFinalField")
public class CachedCompiler {
    private static final Map<ClassLoader, Map<String, Class>> loadedClassesMap = new WeakHashMap<ClassLoader, Map<String, Class>>();

    @Nullable
    private final File sourceDir;
    @Nullable
    private final File classDir;

    private final List<JavaFileObject> javaFileObjects = new ArrayList<JavaFileObject>();

    public CachedCompiler(@Nullable File sourceDir, @Nullable File classDir) {
        this.sourceDir = sourceDir;
        this.classDir = classDir;
    }

    public static void close() {
        try {
            CompilerUtils.s_fileManager.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public Class loadFromJava(@NotNull String className, @NotNull String javaCode, ClassLoader cl, DiagnosticListener diagnosticListener) throws ClassNotFoundException {
        return loadFromJava(cl, className, javaCode, diagnosticListener);
    }

    @NotNull
    Map<String, byte[]> compileFromJava(@NotNull String className, @NotNull String javaCode, DiagnosticListener<JavaFileObject> diagnosticListener) {
        Iterable<? extends JavaFileObject> compilationUnits;
        if (sourceDir != null) {
            String filename = className.replaceAll("\\.", '\\' + File.separator) + ".java";
            File file = new File(sourceDir, filename);
            CompilerUtils.writeText(file, javaCode);
            compilationUnits = CompilerUtils.s_standardJavaFileManager.getJavaFileObjects(file);
        } else {
            javaFileObjects.add(new JavaSourceFromString(className, javaCode));
            compilationUnits = javaFileObjects;
        }

        CompilerUtils.s_compiler.getTask(null, CompilerUtils.s_fileManager, diagnosticListener, null, null, compilationUnits).call();

        return CompilerUtils.s_fileManager.getAllBuffers();
    }

    public Class loadFromJava(@NotNull ClassLoader classLoader, @NotNull String className, @NotNull String javaCode, DiagnosticListener<JavaFileObject> diagnosticListener) throws ClassNotFoundException {
        System.out.println(" --- classLoader lors du loadFromJava de " + className + " -- AVANT");
        debugClassLoader(classLoader);

        synchronized (loadedClassesMap) {
            if (!loadedClassesMap.containsKey(classLoader))
                loadedClassesMap.put(classLoader, new LinkedHashMap<String, Class>());
            else if (getExistingLoadedClassesMap(classLoader).containsKey(className))
                return getExistingLoadedClassesMap(classLoader).get(className);
        }

        for (Map.Entry<String, byte[]> entry : compileFromJava(className, javaCode, diagnosticListener).entrySet()) {
            String className2 = entry.getKey();
            synchronized (loadedClassesMap) {
                if (getExistingLoadedClassesMap(classLoader).containsKey(className2))
                    continue;
            }
            byte[] bytes = entry.getValue();
            if (classDir != null) {
                String filename = className2.replaceAll("\\.", '\\' + File.separator) + ".class";
                boolean changed = CompilerUtils.writeBytes(new File(classDir, filename), bytes);
                if (changed) {
                    LoggerFactory.getLogger(CachedCompiler.class).info("Updated {} in {}", className2, classDir);
                }
            }

            Class clazz2;
            try {
                clazz2 = CompilerUtils.defineClass(classLoader, className2, bytes);
            } catch(AssertionError e){
                System.out.println("classe + " + className2 + " déjà définie");
            }

            synchronized (loadedClassesMap) {
                Class loadedClass = classLoader.loadClass(className2);
                getExistingLoadedClassesMap(classLoader).put(className2, loadedClass);
            }
        }

        System.out.println(" --- classLoader lors du loadFromJava de " + className + " -- APRES BOUCLE COMPILE");
        debugClassLoader(classLoader);
        System.out.println("classe à chercher : " + className);

        Class clazz = classLoader.loadClass(className);
        synchronized (loadedClassesMap) {
            getExistingLoadedClassesMap(classLoader).put(className, clazz);
        }
        return clazz;
    }

    private Map<String, Class> getExistingLoadedClassesMap(@NotNull ClassLoader classLoader) {
        return loadedClassesMap.get(classLoader);
    }

    private void debugClassLoader(@NotNull ClassLoader classLoader) {
        System.out.println("affichage du classloader :");
        try {
            Vector<Class> classes = getAllClassesOf(classLoader);
            for(Class c : classes)
                System.out.println(c.getName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Vector<Class> getAllClassesOf(@NotNull ClassLoader classLoader) throws NoSuchFieldException, IllegalAccessException {
        Field f = ClassLoader.class.getDeclaredField("classes");
        f.setAccessible(true);
        return (Vector<Class>) f.get(classLoader);
    }
}
