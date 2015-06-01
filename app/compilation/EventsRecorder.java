package compilation;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Created by yann on 28/05/15.
 */
public class EventsRecorder {
    private StringListStream outStream;
    private StringListStream errStream;

    public EventsRecorder() throws FileNotFoundException {
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
