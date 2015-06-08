package datas.compilation;

import play.mvc.WebSocket;

import java.io.IOException;


/**
 * Created by yann on 04/06/15.
 */
public class CompileStrategy extends ProcessStrategy {

    public CompileStrategy(String callJavacMain, CompilationAndRunResult compilationAndRunResult, WebSocket.Out out) throws IOException {
        super(callJavacMain,compilationAndRunResult, out);
    }
}
