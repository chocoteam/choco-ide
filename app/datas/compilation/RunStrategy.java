package datas.compilation;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by yann on 04/06/15.
 */
public class RunStrategy extends ProcessStrategy {

    private static final String CHOCO_DEBUG = ".*\\[main\\] DEBUG.*";

    public RunStrategy(String callJavacMain, CompilationAndRunResult compilationAndRunResult) throws IOException {
        super(callJavacMain,compilationAndRunResult);
    }

    public void handleOutputs() {
        String messOut = mapRes.get(RunEvent.Kind.OUT);
        String messErr = mapRes.get(RunEvent.Kind.ERR);

        String filteredMessOut = StringFilter.filterOutLines(messOut, Arrays.asList(/*CHOCO_DEBUG*/));
        if (!"".equals(filteredMessOut)) {
            compilationAndRunResult.addEvent(new RunEvent(filteredMessOut, RunEvent.Kind.OUT.toString(), 0));
        }

        String filteredMessErr = StringFilter.filterOutLines(messErr, Arrays.asList());
        if (!"".equals(filteredMessErr)) {
            compilationAndRunResult.addEvent(new RunEvent(filteredMessErr, RunEvent.Kind.ERR.toString(), 0));
        }
    }
}
