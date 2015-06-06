package datas.compilation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

        if (!"".equals(messErr)) {
            String filteredMess = filterOutLines(messErr);
            this.compilationAndRunResult.addError(filteredMess);
        }
    }

    private String filterOutLines(String messErr) {
        String[] split = messErr.split("\n");
        Collector<CharSequence, ?, String> joining = Collectors.joining("\n");
        return Arrays.stream(split).filter(l -> keepLine(l)).collect(joining);
    }

    public static boolean keepLine(String l) {
        List<String> patternsToRemove = Arrays.asList(STARTING_JAVA_TOOL_OPTIONS);
        // indique si aucun des patterns match la ligne
        return !patternsToRemove.stream().anyMatch(p-> Pattern.compile(p).matcher(l).matches());
    }
}
