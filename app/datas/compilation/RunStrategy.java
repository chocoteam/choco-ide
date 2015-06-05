package datas.compilation;

import java.io.IOException;

/**
 * Created by yann on 04/06/15.
 */
public class RunStrategy extends ProcessStrategy {
    public RunStrategy(String callJavacMain, CompilationAndRunResult compilationAndRunResult) throws IOException {
        super(callJavacMain,compilationAndRunResult);
    }

    public void handleOutputs() {
        String messOut = mapRes.get(RunEvent.Kind.OUT);
        String messErr = mapRes.get(RunEvent.Kind.ERR);

        if(!"".equals(messOut)) {
            compilationAndRunResult.addEvent(new RunEvent(messOut, RunEvent.Kind.OUT.toString(), 0));
        }

        if(!"".equals(messErr))
            compilationAndRunResult.addEvent(new RunEvent(messErr, RunEvent.Kind.ERR.toString(), 0));
    }
}
