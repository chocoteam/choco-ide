package compilation;

import java.io.*;
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

        Pattern pattern = Pattern.compile(PATTERN_MAIN);
        Matcher matcher = pattern.matcher(code);
        while(matcher.find()){
            System.out.println("main class : \"" + matcher.group(1) + "\"");
        }

        compilationAndRunResult = new CompilationAndRunResult();

        List<RunEvent> runEvents = new ArrayList<RunEvent>();
        compileCode(code);

        if(canRunCode()) {
            EventsRecorder eventsRecorder = new EventsRecorder();
            EventsRecorder recorder = eventsRecorder;
            try {
                runCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runEvents.addAll(recorder.getEvents());
        }

        return compilationAndRunResult;
    }

    private boolean canRunCode() {
        return this.compilationAndRunResult.getErrors().isEmpty();
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
        if(!"".equals(s1))
            this.compilationAndRunResult.addError(s1);

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
        if(!"".equals(s))
            compilationAndRunResult.addEvent(new RunEvent(s, RunEvent.Kind.OUT.toString(), 0));

        System.out.println("stderr :");
        BufferedReader stdError = processRunner.getStdError();
        String s1 = stringOfReader(stdError);
        System.out.println("\""+s1+"\"");
        if(!"".equals(s1))
            compilationAndRunResult.addEvent(new RunEvent(s1, RunEvent.Kind.ERR.toString(), 0));
    }
}
