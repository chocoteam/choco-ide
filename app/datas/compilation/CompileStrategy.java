package datas.compilation;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by yann on 04/06/15.
 */
public class CompileStrategy extends ProcessStrategy {

    private static final String STARTING_JAVA_TOOL_OPTIONS = "^Picked up JAVA_TOOL_OPTIONS:.*";

    public CompileStrategy(String callJavacMain, CompilationAndRunResult compilationAndRunResult) throws IOException {
        super(callJavacMain,compilationAndRunResult);
    }

    public void handleOutputs() {
        String messErr = this.mapRes.get(RunEvent.Kind.ERR);

        String filteredMess = StringFilter.filterOutLines(messErr, Arrays.asList(STARTING_JAVA_TOOL_OPTIONS));
        if (!"".equals(filteredMess)) {
            this.compilationAndRunResult.addError(filteredMess);
        }
    }
}
