package datas.compilation;

import play.mvc.WebSocket;

import java.io.IOException;


/**
 * Created by yann on 04/06/15.
 */
public class CompileStrategy extends ProcessStrategy {

    private static final String STARTING_JAVA_TOOL_OPTIONS = "^Picked up JAVA_TOOL_OPTIONS:.*";

    public CompileStrategy(String callJavacMain, CompilationAndRunResult compilationAndRunResult, WebSocket.Out out) throws IOException {
        super(callJavacMain,compilationAndRunResult, out);
    }

    public void handleOutputs() {
        //String messErr = this.mapRes.get(RunEvent.Kind.ERR);

        //String filteredMess = filterOutLines(messErr);
//        if (!"".equals(filteredMess)) {
//            this.compilationAndRunResult.addError(filteredMess);
//        }
    }

//    private String filterOutLines(String messErr) {
//        String[] split = messErr.split("\n");
//        Collector<CharSequence, ?, String> joining = Collectors.joining("\n");
//        return Arrays.stream(split).filter(l -> keepLine(l)).collect(joining);
//    }
//
//    public static boolean keepLine(String l) {
//        List<String> patternsToRemove = Arrays.asList(STARTING_JAVA_TOOL_OPTIONS);
//        // indique si aucun des patterns match la ligne
//        return patternsToRemove.stream().noneMatch(p-> Pattern.compile(p).matcher(l).matches());
//    }
}
