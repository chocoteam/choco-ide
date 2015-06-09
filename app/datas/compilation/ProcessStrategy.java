package datas.compilation;

import datas.compilation.RunEvent.Kind;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by yann on 04/06/15.
 */
public abstract class ProcessStrategy {

    protected final CompilationAndRunResult compilationAndRunResult;
    private final String command;
    protected HashMap<Kind, String> mapRes;

    public ProcessStrategy(String command, CompilationAndRunResult compilationAndRunResult) throws IOException {
        this.command = command;
        this.compilationAndRunResult = compilationAndRunResult;
        mapRes = new HashMap<>();
    }

    public ProcessStrategy executeCommand() throws IOException {
        System.out.println("Executing command : \""+command+"\"");
        Process p = Runtime.getRuntime().exec(command);
        readOutputAndStoreString(p.getInputStream(), Kind.OUT);
        readOutputAndStoreString(p.getErrorStream(), Kind.ERR);
        p.destroy();
        System.out.println("End of command");
        return this;
    }

    private void readOutputAndStoreString(InputStream stream, Kind kind) throws IOException {
        BufferedReader reader = new BufferedReader((new InputStreamReader(stream)));
        mapRes.put(kind, getAndOutput(reader, kind));
    }

    @NotNull
    private String getAndOutput(BufferedReader reader, Kind kind) throws IOException {
        System.out.println(kind + ":");
        String res = stringOfReader(reader);
        System.out.println("\""+res+"\"");
        return res;
    }

    private String stringOfReader(BufferedReader reader) throws IOException {
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public abstract void handleOutputs();
}
