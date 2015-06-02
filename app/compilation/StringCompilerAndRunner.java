package compilation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yann on 16/05/15.
 */
public class StringCompilerAndRunner {

    private static final String MAIN_FILE = "tmp/src/Main.java";
    private static final String CALL_JAVA_MAIN = "java -cp tmp/bin/:lib/* Main";
    private static final String CALL_JAVAC_MAIN = "javac -d tmp/bin/ -cp lib/* tmp/src/Main.java";
    private CompilationAndRunResult compilationAndRunResult;

    public CompilationAndRunResult compileAndRun(String code) throws IOException {
        System.out.println("Debut compileAndRun");

        compilationAndRunResult = new CompilationAndRunResult();

        List<RunEvent> runEvents = new ArrayList<RunEvent>();
        compileCode(code);
        EventsRecorder eventsRecorder = new EventsRecorder();
        OutputRedirector redirector = new OutputRedirector(eventsRecorder);
        try {
            runCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        runEvents.addAll(redirector.getEvents());

        return compilationAndRunResult;
    }

    private void compileCode(String code) throws IOException {
        System.out.println("Compilation en cours");

        createFilesBeforeCompile(code);
        ProcessRunner processRunner = new ProcessRunner(CALL_JAVAC_MAIN);
        processRunner.blockingRun();

        System.out.println("stdout :");
        BufferedReader stdInput = processRunner.getStdInput();
        String s = stringOfReader(stdInput);
        System.out.println("\""+s+"\"");

        System.out.println("stderr :");
        BufferedReader stdError = processRunner.getStdError();
        String s1 = stringOfReader(stdError);
        System.out.println("\""+s1+"\"");
        if(!"".equals(s1)){
            this.compilationAndRunResult.addError(s1);
        }

        System.out.println("Fin de la compilation");
    }

    private String stringOfReader(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null){
            sb.append(line+"\n");
        }

        return sb.toString();
    }

    private void createFilesBeforeCompile(String code) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(MAIN_FILE, "UTF-8");
        writer.println(code);
        writer.close();
    }

    private void runCode() throws IOException {
        ProcessRunner processRunner = new ProcessRunner(CALL_JAVA_MAIN);
        processRunner.blockingRun();

        System.out.println("stdout :");
        BufferedReader stdInput = processRunner.getStdInput();
        String s = stringOfReader(stdInput);
        System.out.println("\""+s+"\"");

        System.out.println("stderr :");
        BufferedReader stdError = processRunner.getStdError();
        String s1 = stringOfReader(stdError);
        System.out.println("\""+s1+"\"");
    }
}
