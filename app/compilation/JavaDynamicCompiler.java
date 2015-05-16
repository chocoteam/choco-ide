package compilation;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.util.Arrays;

public class JavaDynamicCompiler<T> {
    private JavaCompiler compiler;
    private JavaDynamicManager javaDynamicManager;
    private JavaDynamicClassLoader classLoader;
    private DiagnosticCollector<JavaFileObject> diagnostics;

    public JavaDynamicCompiler() throws JavaDynamicException {
        compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new JavaDynamicException("No compiler found");
        }

        classLoader = new JavaDynamicClassLoader(getClass().getClassLoader());
        diagnostics = new DiagnosticCollector<>();

        StandardJavaFileManager standardFileManager = compiler
                .getStandardFileManager(diagnostics, null, null);
        javaDynamicManager = new JavaDynamicManager(standardFileManager, classLoader);
    }

    @SuppressWarnings("unchecked")
    public synchronized Class<T> compile(String packageName, String className,
                                         String javaSource) throws JavaDynamicException {
        try {
            String qualifiedClassName = JavaDynamicUtils.INSTANCE.getQualifiedClassName(
                    packageName, className);
            JavaDynamicBean sourceObj = new JavaDynamicBean(className, javaSource);
            JavaDynamicBean compiledObj = new JavaDynamicBean(qualifiedClassName);
            javaDynamicManager.setSources(sourceObj, compiledObj);

            CompilationTask task = compiler.getTask(null, javaDynamicManager, diagnostics,
                    null, null, Arrays.asList(sourceObj));
            boolean result = task.call();

            if (!result) {
                throw new JavaDynamicException("Compilation failed", diagnostics);
            }

            return (Class<T>) classLoader.loadClass(qualifiedClassName);

        } catch (Exception exception) {
            throw new JavaDynamicException(exception, diagnostics);
        }
    }
}
