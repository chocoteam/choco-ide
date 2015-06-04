package compilation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yann on 16/05/15.
 */
public class StringCompilerAndRunner {

    // Commandes utilisées à la compilation
    private static final String MAIN_FILE = "ctmp/src/Main.java";
    private static final String CALL_JAVA_MAIN = "java -cp ctmp/bin/:lib/* Main";
    private static final String CALL_JAVAC_MAIN = "javac -d ctmp/bin/ -cp lib/* ctmp/src/Main.java";

    // Regex permettant de trouver le nom de la classe possédant la méthode main (dans le 1er group)
    private static final String PATTERN_MAIN = "public class (\\w*)\\s\\{[\\n|\\s]*\\s*public static void main";

    private CompilationAndRunResult compilationAndRunResult;

    public CompilationAndRunResult compileAndRun(String code) throws IOException {
        System.out.println("Debut compileAndRun");

        findMainClass(code);

        compilationAndRunResult = new CompilationAndRunResult();

        createFilesBeforeCompile(code);
        compileCode(code, compilationAndRunResult);
        List<RunEvent> runEvents = new ArrayList<RunEvent>();

        if(canRunCode()) {
            EventsRecorder eventsRecorder = new EventsRecorder();
            EventsRecorder recorder = eventsRecorder;
            try {
                runCode(compilationAndRunResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
            runEvents.addAll(recorder.getEvents());
        }

        return compilationAndRunResult;
    }

    private void findMainClass(String code) {
        Pattern pattern = Pattern.compile(PATTERN_MAIN);
        Matcher matcher = pattern.matcher(code);
        while(matcher.find()){
            System.out.println("main class : \"" + matcher.group(1) + "\"");
        }
    }

    private boolean canRunCode() {
        return this.compilationAndRunResult.getErrors().isEmpty();
    }

    private void compileCode(String code, CompilationAndRunResult compilationAndRunResult) throws IOException {
        new CompileStrategy(CALL_JAVAC_MAIN, compilationAndRunResult).handleOutputs();
    }

    private void runCode(CompilationAndRunResult compilationAndRunResult) throws IOException {
        new RunStrategy(CALL_JAVA_MAIN, compilationAndRunResult).handleOutputs();
    }

    private void createFilesBeforeCompile(String code) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(MAIN_FILE, "UTF-8");
        writer.println(code);
        writer.close();
    }


}
