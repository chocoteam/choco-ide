package compilation;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.io.FileNotFoundException;
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

        List<RunEvent> runEvents = new ArrayList<RunEvent>();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();

        try {
            ChocoProject instance = compileCode(code, collector);

            EventsRecorder eventsRecorder = new EventsRecorder();
            OutputRedirector redirector = new OutputRedirector(eventsRecorder);
            runCode(instance);
            redirector.restore();

            runEvents.addAll(redirector.getEvents());

        } catch (ClassNotFoundException e) {
            System.err.println("Erreur à la compilation !");
        } catch (FileNotFoundException e) {
            System.err.println("Erreur de la création des streams ! " + e.getMessage());
        }

        return new CompilationAndRunResult(collector, runEvents);
    }

    private ChocoProject compileCode(String code, DiagnosticCollector<JavaFileObject> collector) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        CachedCompiler cc = new CachedCompiler(null, null);
        Class clazz = cc.loadFromJava("compilation.ChocoProjectImpl", code, cl, collector);
        return (ChocoProject) clazz.newInstance();
    }

    private void runCode(ChocoProject instance) {
        try {
            instance.run();
        } catch (Exception e) {
            System.err.println("Erreur à l'execution !");
            throw e;
        }
    }

    private void debugDiagnostics(DiagnosticCollector<JavaFileObject> collector) {
        collector.getDiagnostics().forEach(System.out::println);
    }
}
