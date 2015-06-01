package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import compilation.CompilationAndRunResult;
import compilation.StringCompilerAndRunner;
import datas.keywords.KeywordsManager;
import datas.report.Report;
import datas.report.ReportManager;
import datas.samples.Sample;
import datas.samples.SampleManager;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

public class Application extends Controller {

    private static final String INTERFACE_CHOCO = "interface ChocoProject {" + "\n"
            + " void run();" + "\n"
            + "}" + "\n";

    public static Result index() {
        return ok(index.render("Let's get working guys !"));
    }

    /**
     * Call to compilation service
     *
     * @return result compilation result
     */
    public static Result compile() throws IllegalAccessException, InstantiationException {
        try {
            final Map<String, String[]> mapParameters = request().body().asFormUrlEncoded();
            String code = mapParameters.get("body")[0] + INTERFACE_CHOCO;
            System.out.println("Code reçu : " + code);
            ClassLoader cl = Play.application().classloader();
            //MyClassLoader tempCl = new MyClassLoader(cl);

            StringCompilerAndRunner compilerAndRunner = new StringCompilerAndRunner(cl);
            CompilationAndRunResult result = compilerAndRunner.compileAndRun(code);
            return ok(new ObjectMapper().<JsonNode>valueToTree(result));
        } catch(Exception e){
            Logger.warn("Problem while compiling and running", e);
            return internalServerError("Problem while compiling and running : "+e.getMessage());
        }
    }

    public static Result getCodeSampleList() {
        try {
            Collection<Sample> availableSample = SampleManager.getInstance().getAvailableSample();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(availableSample);
            return ok(json);
        } catch (Exception e) {
            Logger.warn("Problem while getting the samples", e);
            return internalServerError("Problem while getting the samples : " + e.getMessage());
        }
    }

    public static Result reportError() {
        final Map<String, String[]> mapParameters = request().body().asFormUrlEncoded();
        String sourceCode = mapParameters.get("sourceCode")[0];
        String comment = mapParameters.get("comment")[0];
        String userEmail = mapParameters.get("userEmail")[0];
        String stdOut = mapParameters.get("stdOut")[0];
        String stdErr = mapParameters.get("stdErr")[0];
        String compilationErr = mapParameters.get("compilationErr")[0];

        Report report;
        //Compilation error
        if (stdErr == null || stdErr.trim().isEmpty()) {
            report = ReportManager.getInstance().createReportCompilation(comment,sourceCode, stdOut, compilationErr, userEmail);
        }
        //Execution error
        else {
            report = ReportManager.getInstance().createReportExecution(comment,sourceCode, stdOut, stdErr, userEmail);
        }
        boolean sent = false;
        if (Report.isValidEmail(report.getSenderEmail())) {
            sent = ReportManager.getInstance().sendReport(report);
        }
        if (!sent) {
            return internalServerError();
        } else {
            return ok();
        }
    }

    public static Result getKeywords() throws IOException, ClassNotFoundException {
        String chocoClasses = KeywordsManager.getChocoClassesName();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(chocoClasses);
        return ok(json);
    }

    /**
     * Small piece of code in order to try the execution of processes within Heroku
     * If possible, this method (after some refactoring) will replace the current 'compile' handler
     * @return HTTP 200 if compilation+execution went OK, HTTP 500 otherwise
     */
    public static Result testJava() {
        try {
            // Writing Java Source Code to a file
            PrintWriter writer = new PrintWriter("Main.java", "UTF-8");
            writer.println("class Main {\n" +
                    "   public static void main (String[] args){\n" +
                    "    System.out.println(\"Hello World\");\n" +
                    "   }\n" +
                    "}");
            writer.close();

            // Compiling Java Source Code
            runProcess("javac Main.java");

            // Executing Java Source Code
            runProcess("java Main");

            return ok();

        } catch (Exception e) {
            e.printStackTrace();
            return internalServerError(e.getMessage());
        }

    }

    private static void runProcess(String command) throws IOException {
        Process p;
        String s;
        p = Runtime.getRuntime().exec(command);
        BufferedReader stdInput = new BufferedReader((new InputStreamReader(p.getInputStream())));
        BufferedReader stdError = new BufferedReader((new InputStreamReader(p.getErrorStream())));

        // read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
        // read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }
    }

}
