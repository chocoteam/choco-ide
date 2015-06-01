package compilation;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yann on 26/05/15.
 */
public class CompilationAndRunResult {

    private final List<String> errors;
    private final List<RunEvent> events;

    public CompilationAndRunResult(DiagnosticCollector<JavaFileObject> diag, List<RunEvent> runEvents) {
        errors = diag.getDiagnostics().stream()
                .map(d -> d.toString())
                .collect(Collectors.toList());

        events = runEvents;
    }


    public List<String> getErrors() {
        return errors;
    }

    public List<RunEvent> getEvents() {
        return events;
    }
}
