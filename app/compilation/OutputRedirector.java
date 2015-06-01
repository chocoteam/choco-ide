package compilation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yann on 28/05/15.
 */
public class OutputRedirector {

    private final EventsRecorder eventsRecorder;
    private PrintStream oldOut;
    private PrintStream oldErr;

    public OutputRedirector(EventsRecorder eventsRecorder) {
        this.eventsRecorder = eventsRecorder;
        saveOutputs();
        loadOutputs(eventsRecorder.getOutStream(), eventsRecorder.getErrStream());
    }

    private void loadOutputs(PrintStream outStream, PrintStream errStream) {
        System.setOut(outStream);
        System.setErr(errStream);
    }

    private void saveOutputs() {
        this.oldOut = System.out;
        this.oldErr = System.err;
    }

    public void restore() {
        this.loadOutputs(oldOut, oldErr);
    }

    public List<RunEvent> getEvents() {
        ArrayList<RunEvent> runEvents = new ArrayList<>();
        StringListStream errStream = eventsRecorder.getErrStream();
        StringListStream outStream = eventsRecorder.getOutStream();
        loadEvents(runEvents, errStream, RunEvent.Kind.ERR);
        loadEvents(runEvents, outStream, RunEvent.Kind.OUT);
        return runEvents;
    }

    private void loadEvents(ArrayList<RunEvent> runEvents, StringListStream stream, RunEvent.Kind kind) {
        runEvents.addAll(stream.getList().stream()
                .map(s -> { return new RunEvent(s, kind.toString(), 0); })
                .collect(Collectors.toList()));
    }
}
