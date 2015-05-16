package compilation;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import java.io.IOException;

public class JavaDynamicManager extends ForwardingJavaFileManager<JavaFileManager> {
    private JavaDynamicClassLoader classLoader;

    private JavaDynamicBean source;

    private JavaDynamicBean compiled;

    public JavaDynamicManager(JavaFileManager fileManager, JavaDynamicClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    public void setSources(JavaDynamicBean sourceObject, JavaDynamicBean compiledObject) {
        this.source = sourceObject;
        this.compiled = compiledObject;
        this.classLoader.addClass(compiledObject);
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName,
                                      String relativeName) throws IOException {
        return source;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
                                               String qualifiedName, Kind kind, FileObject outputFile)
            throws IOException {
        return compiled;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }
}