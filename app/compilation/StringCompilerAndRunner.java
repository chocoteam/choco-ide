package compilation;

import net.openhft.compiler.CachedCompiler;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yann on 16/05/15.
 */
public class StringCompilerAndRunner {
    private final ClassLoader cl;

    public StringCompilerAndRunner(ClassLoader cl) {
        this.cl = cl;
    }

    public CompilationAndRunResult compileAndRun(String code) throws IllegalAccessException, InstantiationException {
        CachedCompiler cc = new CachedCompiler(null, null);
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();


        List<RunEvent> runEvents = new ArrayList<RunEvent>();

        try {
            Class clazz = cc.loadFromJava("compilation.ChocoProjectImpl", code, cl, collector);
            ChocoProject instance = (ChocoProject) clazz.newInstance();
            instance.init();
            instance.run();
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur à la compilation !");
        }

        return new CompilationAndRunResult(collector, runEvents);
    }

    private void debugDiagnostics(DiagnosticCollector<JavaFileObject> collector) {
        collector.getDiagnostics().forEach(System.out::println);
    }
}
