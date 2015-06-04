package compilation;

import java.io.IOException;

/**
 * Created by yann on 04/06/15.
 */
public class RunStrategy extends ProcessStrategy {
    public RunStrategy(String callJavacMain, CompilationAndRunResult compilationAndRunResult) throws IOException {
        super(callJavacMain,compilationAndRunResult);
    }

    public void handleOutputs() {
        if(!"".equals(sOut)) {
            compilationAndRunResult.addEvent(new RunEvent(sOut, RunEvent.Kind.OUT.toString(), 0));
        }

        if(!"".equals(sErr))
            compilationAndRunResult.addEvent(new RunEvent(sErr, RunEvent.Kind.ERR.toString(), 0));
    }
}
