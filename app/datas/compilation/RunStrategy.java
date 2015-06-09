package datas.compilation;

import play.Play;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Created by yann on 04/06/15.
 */
public class RunStrategy extends ProcessStrategy {

    /**
     * Pattern d'exécution
     * $1 : tmp folder
     * $2 : libpath
     * $3 : classname
     */
    private static final String CALL_JAVA_MAIN = "java -Djava.security.manager -Djava.security.policy=="+ Play.application().configuration().getString("security.manager.path")
            +" -cp %1$s/bin/"+ File.pathSeparator + "%2$s"+File.pathSeparator+" -Dlogback.configurationFile=lib/logback.xml %3$s";

    private static final String CHOCO_DEBUG = ".*\\[main\\] DEBUG.*";

    public RunStrategy(CompilationAndRunResult compilationAndRunResult, Path tempDirectory, String libpath, String className) throws IOException {
        super(String.format(CALL_JAVA_MAIN, tempDirectory.toString(), libpath, className),compilationAndRunResult);
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
