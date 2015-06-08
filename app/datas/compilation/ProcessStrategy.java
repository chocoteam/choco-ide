package datas.compilation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.mvc.WebSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Created by yann on 04/06/15.
 */
public abstract class ProcessStrategy {

    protected final CompilationAndRunResult compilationAndRunResult;
    protected WebSocket.Out out;

    private Pattern pattern = Pattern.compile("DEBUG");

    public ProcessStrategy(String command, CompilationAndRunResult compilationAndRunResult, WebSocket.Out out) throws IOException {
        this.compilationAndRunResult = compilationAndRunResult;
        Process p = Runtime.getRuntime().exec(command);

        this.out = out;

        readOutputAndStoreString(p.getInputStream(), RunEvent.Kind.OUT);
        readOutputAndStoreString(p.getErrorStream(), RunEvent.Kind.ERR);


    }

    private void readOutputAndStoreString(InputStream stream, RunEvent.Kind kind) throws IOException {
        BufferedReader reader = new BufferedReader((new InputStreamReader(stream)));
        ObjectMapper mapper = new ObjectMapper();

        reader.lines().forEach(x->{
            if (!pattern.matcher(x).find()) {
                Message m = new Message(x,kind);
                try {
                    out.write(mapper.writeValueAsString(m));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

class Message {
    public String message;
    public RunEvent.Kind kind;

    public Message(String message, RunEvent.Kind kind){
        this.message=message;
        this.kind=kind;
    }
}
