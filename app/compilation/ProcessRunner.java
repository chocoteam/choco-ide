package compilation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by yann on 02/06/15.
 */
public class ProcessRunner {
    private final String command;
    private HashMap<RunEvent.Kind, BufferedReader> mapReaders;

    public ProcessRunner(String command){
        this.command = command;
    }

    public void blockingRun() throws IOException {
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader stdInput = new BufferedReader((new InputStreamReader(p.getInputStream())));
        BufferedReader stdError = new BufferedReader((new InputStreamReader(p.getErrorStream())));
        mapReaders = new HashMap<>();
        mapReaders.put(RunEvent.Kind.OUT, stdInput);
        mapReaders.put(RunEvent.Kind.ERR, stdError);
    }


    public BufferedReader getOutput(RunEvent.Kind kind) {
        return mapReaders.get(kind);
    }
}
