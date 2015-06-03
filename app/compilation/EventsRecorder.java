package compilation;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yann on 28/05/15.
 */
public class EventsRecorder {
    private StringListStream outStream;
    private StringListStream errStream;

    public EventsRecorder() {
        outStream = new StringListStream(new ByteArrayOutputStream());
        errStream = new StringListStream(new ByteArrayOutputStream());
    }

    public StringListStream getOutStream() {
        return outStream;
    }

    public StringListStream getErrStream() {
        return errStream;
    }

    public List<RunEvent> getEvents() {
        ArrayList<RunEvent> runEvents = new ArrayList<>();
        StringListStream errStream = this.getErrStream();
        StringListStream outStream = this.getOutStream();
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
