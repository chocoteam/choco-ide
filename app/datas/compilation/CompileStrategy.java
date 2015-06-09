package datas.compilation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Created by yann on 04/06/15.
 */
public class CompileStrategy extends ProcessStrategy {

    /**
     * Pattern de datas.compilation
     * $1 : tmp folder
     * $2 : libpath
     * $3 : classname
     */
    private static final String CALL_JAVAC_MAIN = "javac -cp %1$s/bin/"+ File.pathSeparator + "%2$s -d %1$s/bin/ %1$s/src/%3$s.java -Xlint:unchecked";

    private static final String STARTING_JAVA_TOOL_OPTIONS = "^Picked up JAVA_TOOL_OPTIONS:.*";

    public CompileStrategy(CompilationAndRunResult compilationAndRunResult, Path tempDirectory, String libpath, String className) throws IOException {
        super(String.format(CALL_JAVAC_MAIN, tempDirectory.toString(), libpath, className),compilationAndRunResult);
    }

    public void handleOutputs() {
        String messErr = this.mapRes.get(RunEvent.Kind.ERR);

        String filteredMess = StringFilter.filterOutLines(messErr, Arrays.asList(STARTING_JAVA_TOOL_OPTIONS));
        if (!"".equals(filteredMess)) {
            this.compilationAndRunResult.addError(filteredMess);
        }
    }
}
