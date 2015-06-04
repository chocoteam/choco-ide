package compilation;

import java.io.IOException;

/**
 * Created by yann on 04/06/15.
 */
public class CompileStrategy extends ProcessStrategy {
    public CompileStrategy(String callJavacMain, CompilationAndRunResult compilationAndRunResult) throws IOException {
        super(callJavacMain,compilationAndRunResult);
    }

    public void handleOutputs() {
        if (!"".equals(sErr))
            this.compilationAndRunResult.addError(sErr);
    }
}
