package compilation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yann on 16/05/15.
 */
public class StringCompilerAndRunner {

    // Commandes utilisées à la compilation
    private static final String ROOT_COMPILE = "ctmp/";
    private static final String MAIN_FILE = ROOT_COMPILE+"src/%s.java";
    private static final String CALL_JAVA_MAIN = "java -cp " + ROOT_COMPILE +"bin/"+ File.pathSeparator +"lib/* %s";
    private static final String CALL_JAVAC_MAIN = "javac -d " + ROOT_COMPILE + "bin/ -cp lib/* " + ROOT_COMPILE + "src/%s.java";

    // Regex permettant de trouver le nom de la classe possédant la méthode main (dans le 1er group)
    private static final String PATTERN_MAIN = "public class (\\w*)\\s\\{[\\n|\\s]*\\s*public static void main";

    private CompilationAndRunResult compilationAndRunResult;

    public CompilationAndRunResult compileAndRun(String code) throws IOException {
        System.out.println("Debut compileAndRun");

        String className = findMainClass(code).orElse("Main");

        compilationAndRunResult = new CompilationAndRunResult();

        createFilesBeforeCompile(code, className);
        compileCode(code, compilationAndRunResult, className);
        List<RunEvent> runEvents = new ArrayList<RunEvent>();

        if(canRunCode()) {
            try {
                runCode(compilationAndRunResult, className);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Fin compileAndRun");

        return compilationAndRunResult;
    }

    private Optional<String> findMainClass(String code) {
        Pattern pattern = Pattern.compile(PATTERN_MAIN);
        Matcher matcher = pattern.matcher(code);
        while(matcher.find()){
            String name = matcher.group(1);
            System.out.println("main class : \"" + name + "\"");
            return Optional.of(name);
        }

        return Optional.empty();
    }

    private boolean canRunCode() {
        return this.compilationAndRunResult.getErrors().isEmpty();
    }

    private void compileCode(String code, CompilationAndRunResult compilationAndRunResult, String className) throws IOException {
        String commande = String.format(CALL_JAVAC_MAIN, className);
        new CompileStrategy(commande, compilationAndRunResult).handleOutputs();
    }

    private void runCode(CompilationAndRunResult compilationAndRunResult, String className) throws IOException {
        String commande = String.format(CALL_JAVA_MAIN, className);
        new RunStrategy(commande, compilationAndRunResult).handleOutputs();
    }

    private void createFilesBeforeCompile(String code, String className) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(String.format(MAIN_FILE, className), "UTF-8");
        writer.println(code);
        writer.close();
    }


}
