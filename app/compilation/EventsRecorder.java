package compilation;

import java.io.ByteArrayOutputStream;

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
}
