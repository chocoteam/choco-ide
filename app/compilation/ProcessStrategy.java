package compilation;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by yann on 04/06/15.
 */
public abstract class ProcessStrategy {

    protected final CompilationAndRunResult compilationAndRunResult;
    protected final String sOut;
    protected final String sErr;

    public ProcessStrategy(String command, CompilationAndRunResult compilationAndRunResult) throws IOException {
        this.compilationAndRunResult = compilationAndRunResult;
        ProcessRunner processRunner = new ProcessRunner(command);
        processRunner.blockingRun();

        sOut = getAndOutput(processRunner, RunEvent.Kind.OUT);
        sErr = getAndOutput(processRunner, RunEvent.Kind.ERR);
    }

    @NotNull
    private String getAndOutput(ProcessRunner processRunner, RunEvent.Kind kind) throws IOException {
        System.out.println(kind + ":");
        String res = stringOfReader(processRunner.getOutput(kind));
        System.out.println("\""+res+"\"");
        return res;
    }

    private String stringOfReader(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null){
            sb.append(line+"\n");
        }

        return sb.toString();
    }

    public abstract void handleOutputs();
}
