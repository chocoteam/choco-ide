package compilation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yann on 26/05/15.
 */
public class CompilationAndRunResult {

    private final List<String> errors;
    private final List<RunEvent> events;

    public CompilationAndRunResult(List<String> compilErrors, List<RunEvent> runEvents) {
        errors = compilErrors;
        events = runEvents;
    }

    public CompilationAndRunResult() {
        this.errors = new ArrayList<>();
        this.events = new ArrayList<>();
    }


    public List<String> getErrors() {
        return errors;
    }

    public List<RunEvent> getEvents() {
        return events;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public void addEvent(RunEvent runEvent) {
        this.events.add(runEvent);
    }
}
