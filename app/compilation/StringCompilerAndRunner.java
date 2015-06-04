package compilation;

import play.Play;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    /**
     * Pattern du chemin du fichier créé
     * $1 : tmp folder
     * $2 : classname
     */
    private static final String MAIN_FILE = "%s/src/%s.java";

    /**
     * Pattern de compilation
     * $1 : tmp folder
     * $2 : libpath
     * $3 : classname
     */
    private static final String CALL_JAVAC_MAIN = "javac -cp %1$s/bin/"+ File.pathSeparator + "%2$s -d %1$s/bin/ %1$s/src/%3$s.java";

    /**
     * Pattern d'exécution
     * $1 : tmp folder
     * $2 : libpath
     * $3 : classname
     */
    private static final String CALL_JAVA_MAIN = "java -cp %1$s/bin/"+ File.pathSeparator + "%2$s %3$s";

    // Regex permettant de trouver le nom de la classe possédant la méthode main (dans le 1er group)
    private static final String PATTERN_MAIN = "public class (\\w*)\\s\\{[\\n|\\s]*\\s*public static void main";

    private CompilationAndRunResult compilationAndRunResult;
    private Path tempDirectory;

    public CompilationAndRunResult compileAndRun(String code) throws IOException {
        System.out.println("Debut compileAndRun");


        String className = findMainClass(code).orElse("Main");

        compilationAndRunResult = new CompilationAndRunResult();

        String libpath = Play.application().configuration().getString("compilation.libpath");
        String folderTmpCompile = Play.application().configuration().getString("compilation.tmpPath");
        tempDirectory = Files.createTempDirectory(Paths.get(folderTmpCompile), "");

        createFilesBeforeCompile(code, className);
        compileCode(code, compilationAndRunResult, className, libpath);
        List<RunEvent> runEvents = new ArrayList<RunEvent>();

        if(canRunCode()) {
            try {
                runCode(compilationAndRunResult, className, libpath);
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

    private void compileCode(String code, CompilationAndRunResult compilationAndRunResult, String className, String libpath) throws IOException {
        String commande = String.format(CALL_JAVAC_MAIN, tempDirectory.toString(), libpath, className);
        new CompileStrategy(commande, compilationAndRunResult).handleOutputs();
    }

    private void runCode(CompilationAndRunResult compilationAndRunResult, String className, String libpath) throws IOException {
        String commande = String.format(CALL_JAVA_MAIN, tempDirectory.toString(), libpath, className);
        new RunStrategy(commande, compilationAndRunResult).handleOutputs();
    }

    private void createFilesBeforeCompile(String code, String className) throws IOException {
        Files.createDirectories(tempDirectory.resolve("bin"));
        Files.createDirectories(tempDirectory.resolve("src"));
        PrintWriter writer = new PrintWriter(String.format(MAIN_FILE, tempDirectory.toString(), className), "UTF-8");
        writer.println(code);
        writer.close();
    }


}
