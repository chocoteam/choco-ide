package datas.compilation;

import datas.Utils.FileUtils;
import play.Play;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yann on 16/05/15.
 */
public class StringCompilerAndRunner {

    // Commandes utilisées à la datas.compilation
    /**
     * Pattern du chemin du fichier créé
     * $1 : tmp folder
     * $2 : classname
     */
    private static final String MAIN_FILE = "%s/src/%s.java";

    // Regex permettant de trouver le nom de la classe possédant la méthode main (dans le 1er group)
    private static final String PATTERN_MAIN = "public class ([A-Za-z_\\$]+)";

    private static final String PATTERN_PACKAGE = "package ([A-Za-z_\\$]+(\\.[A-Za-z_\\$]+)*);";

    public CompilationAndRunResult compileAndRun(String code) throws IOException {
        System.out.println("Debut compileAndRun");

        String libPath = Play.application().configuration().getString("datas.compilation.libPath");
        String tmpPath = Play.application().configuration().getString("datas.compilation.tmpPath");
        Files.createDirectories(Paths.get(tmpPath));
        Path tempDirectory = Files.createTempDirectory(Paths.get(tmpPath), "choco-");

        String className = findMainClass(code).orElse("Main");
        String packageName = findPackage(code).orElse("");
        String fullClassName = packageName.equals("")?className:packageName+"."+className;
        System.out.println("Full name : " + fullClassName);

        createFilesBeforeCompile(code, className, tempDirectory);
        CompilationAndRunResult compilationAndRunResult = new CompilationAndRunResult();
        compileCode(compilationAndRunResult, className, libPath, tempDirectory);
        runCode(compilationAndRunResult, fullClassName, libPath, tempDirectory);

        //deleteTmpFolder(tempDirectory);

        System.out.println("Fin compileAndRun");

        return compilationAndRunResult;
    }

    private void deleteTmpFolder(Path tempDirectory) {
        FileUtils.recursiveDelete(tempDirectory);
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

    private Optional<String> findPackage(String code) {
        Pattern pattern = Pattern.compile(PATTERN_PACKAGE);
        Matcher matcher = pattern.matcher(code);
        while(matcher.find()){
            String name = matcher.group(1);
            System.out.println("package : \"" + name + "\"");
            return Optional.of(name);
        }

        return Optional.empty();
    }

    private void compileCode(CompilationAndRunResult compilationAndRunResult, String className, String libpath, Path tempDirectory) throws IOException {
        new CompileStrategy(compilationAndRunResult, tempDirectory, libpath, className).executeCommand().handleOutputs();
    }

    private void runCode(CompilationAndRunResult compilationAndRunResult, String className, String libpath, Path tempDirectory) throws IOException {
        new RunStrategy(compilationAndRunResult, tempDirectory, libpath, className).executeCommand().handleOutputs();
    }

    private void createFilesBeforeCompile(String code, String className, Path tempDirectory) throws IOException {
        List<String> folders = Arrays.asList(new String[]{"bin", "src"});
        folders.forEach(f -> {
            try {
                createFolderInTmp(f, tempDirectory);
            } catch (IOException e) {
                System.err.println("erreur à la création de " + f + " : " + e.getMessage());
            }
        });
        createJavaSourceFile(code, className, tempDirectory);
    }

    private void createJavaSourceFile(String code, String className, Path tempDirectory) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(String.format(MAIN_FILE, tempDirectory.toString(), className), "UTF-8");
        writer.println(code);
        writer.close();
    }

    private Path createFolderInTmp(String folderName, Path tempDirectory) throws IOException {
        Path path = tempDirectory.resolve(folderName);
        Files.createDirectories(path);
        return path;
    }



}
