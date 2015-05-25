package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import compilation.StringCompilerAndRunner;
import datas.samples.Sample;
import datas.samples.SampleManager;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.Collection;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Let's get working guys !"));
    }

    /**
     * Call to compilation service
     *
     * @return result compilation result
     */
    public static Result compile() throws IllegalAccessException, InstantiationException {
        System.out.println(request().body());
//        final Map<String, String[]> mapParameters = request().body().asFormUrlEncoded();
//        String code = mapParameters.get("body")[0];
        String code = "package compilation;" + "\n"
                + "class ChocoProjectImpl implements ChocoProject {" + "\n"
                + " @Override" + "\n"
                + " public void init() {" + "\n"
                + "     System.out.println(\"... init du code compilé ...\");" + "\n"
                + " }" + "\n"
                + " @Override" + "\n"
                + " public void run() {" + "\n"
                + "     System.out.println(\"... run du code ...\");" + "\n"
                + " }" + "\n"
                + "}" + "\n"
                + "interface ChocoProject {" + "\n"
                + " void init();" + "\n"
                + " void run();"+"\n"
                + "}"+"\n";

        System.out.println(code);
        compileAndRunFromString(code);
        return ok("compilation OK");
    }

    private static void compileAndRunFromString(String code) throws InstantiationException, IllegalAccessException {
        ClassLoader cl = Play.application().classloader();
        StringCompilerAndRunner compilerAndRunner = new StringCompilerAndRunner(cl);
        try {
            compilerAndRunner.compileAndRun(code);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Result getCodeSampleList(){
        try {
            Collection<Sample> availableSample = SampleManager.getInstance().getAvailableSample();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(availableSample);
            return ok(json);
        }catch(Exception e){
            Logger.warn("Problem while getting the samples", e);
            return internalServerError("Problem while getting the samples : "+e.getMessage());
        }
    }

    public static Result reportError() {
        return play.mvc.Results.TODO;
    }
}