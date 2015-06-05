package datas.compilation;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by yann on 04/06/15.
 */
public abstract class ProcessStrategy {

    protected final CompilationAndRunResult compilationAndRunResult;
    protected HashMap<RunEvent.Kind, String> mapRes;

    public ProcessStrategy(String command, CompilationAndRunResult compilationAndRunResult) throws IOException {
        this.compilationAndRunResult = compilationAndRunResult;
        System.out.println("JAVA_TOOL_OPTIONS = " + System.getenv("JAVA_TOOL_OPTIONS"));
        Process p = Runtime.getRuntime().exec(command);
        mapRes = new HashMap<>();

        readOutputAndStoreString(p.getInputStream(), RunEvent.Kind.OUT);
        readOutputAndStoreString(p.getErrorStream(), RunEvent.Kind.ERR);
    }

    private void readOutputAndStoreString(InputStream stream, RunEvent.Kind kind) throws IOException {
        BufferedReader reader = new BufferedReader((new InputStreamReader(stream)));
        mapRes.put(kind, getAndOutput(reader, kind));
    }

    @NotNull
    private String getAndOutput(BufferedReader reader, RunEvent.Kind kind) throws IOException {
        System.out.println(kind + ":");
        String res = stringOfReader(reader);
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
