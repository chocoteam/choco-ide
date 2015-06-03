package compilation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by yann on 02/06/15.
 */
public class ProcessRunner {
    private final String command;
    private BufferedReader stdInput;
    private BufferedReader stdError;

    public ProcessRunner(String command){
        this.command = command;
    }

    public void blockingRun() throws IOException {
        Process p = Runtime.getRuntime().exec(command);
        stdInput = new BufferedReader((new InputStreamReader(p.getInputStream())));
        stdError = new BufferedReader((new InputStreamReader(p.getErrorStream())));
    }

    public BufferedReader getStdInput() {
        return stdInput;
    }

    public BufferedReader getStdError() {
        return stdError;
    }
}
