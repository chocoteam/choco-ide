package compilation;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by yann on 04/06/15.
 */
public abstract class ProcessStrategy {

    protected final CompilationAndRunResult compilationAndRunResult;
    protected final String sOut;
    protected final String sErr;
    private final HashMap<RunEvent.Kind, BufferedReader> mapReaders;

    public ProcessStrategy(String command, CompilationAndRunResult compilationAndRunResult) throws IOException {
        this.compilationAndRunResult = compilationAndRunResult;
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader stdInput = new BufferedReader((new InputStreamReader(p.getInputStream())));
        BufferedReader stdError = new BufferedReader((new InputStreamReader(p.getErrorStream())));
        mapReaders = new HashMap<>();
        mapReaders.put(RunEvent.Kind.OUT, stdInput);
        mapReaders.put(RunEvent.Kind.ERR, stdError);

        sOut = getAndOutput(mapReaders, RunEvent.Kind.OUT);
        sErr = getAndOutput(mapReaders, RunEvent.Kind.ERR);
    }

    @NotNull
    private String getAndOutput(HashMap<RunEvent.Kind, BufferedReader> readerMap, RunEvent.Kind kind) throws IOException {
        System.out.println(kind + ":");
        String res = stringOfReader(readerMap.get(kind));
        System.out.println("\""+res+"\"");
        return res;
    }

    private String stringOfReader(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null){
            sb.append(line+"\n");
        }

        return sb.toString();
    }

    public abstract void handleOutputs();
}
