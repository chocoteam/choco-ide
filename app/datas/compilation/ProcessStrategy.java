package datas.compilation;

import play.mvc.WebSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yann on 04/06/15.
 */
public abstract class ProcessStrategy {

    protected final CompilationAndRunResult compilationAndRunResult;
    protected WebSocket.Out out;
    //protected HashMap<RunEvent.Kind, String> mapRes;

    public ProcessStrategy(String command, CompilationAndRunResult compilationAndRunResult, WebSocket.Out out) throws IOException {
        this.compilationAndRunResult = compilationAndRunResult;
        Process p = Runtime.getRuntime().exec(command);
        //mapRes = new HashMap<>();

        this.out = out;

        readOutputAndStoreString(p.getInputStream(), RunEvent.Kind.OUT);
        readOutputAndStoreString(p.getErrorStream(), RunEvent.Kind.ERR);
    }

    private void readOutputAndStoreString(InputStream stream, RunEvent.Kind kind) throws IOException {
        BufferedReader reader = new BufferedReader((new InputStreamReader(stream)));

        reader.lines().forEach(x->out.write(x+"\n"));
        //mapRes.put(kind, getAndOutput(reader, kind));
    }

//    @NotNull
//    private String getAndOutput(BufferedReader reader, RunEvent.Kind kind) throws IOException {
//        System.out.println(kind + ":");
////        String res = stringOfReader(reader);
////        System.out.println("\""+res+"\"");
//        return res;
//    }
//
////    private String stringOfReader(BufferedReader reader) throws IOException {
////        return reader.lines().collect(Collectors.joining("\n"));
////    }

    public abstract void handleOutputs();
}
