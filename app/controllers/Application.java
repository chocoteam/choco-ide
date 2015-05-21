package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import compilation.StringCompilerAndRunner;
import datas.samples.Sample;
import datas.samples.SampleManager;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.Collection;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Let's get working guys !"));
    }

    /**
     * Call to compilation service
     *
     * @return result compilation result
     */
    public static Result compile() {
        System.out.println(request().body());
        final Map<String, String[]> mapParameters = request().body().asFormUrlEncoded();
        String code = mapParameters.get("body")[0];
        System.out.println(code);
        //compileAndRunFromString(code);
        return ok("compilation OK");
    }

    private static void compileAndRunFromString() {
        StringCompilerAndRunner compilerAndRunner = new StringCompilerAndRunner();
        String code = "code";
        try {
            compilerAndRunner.compileAndRun(code);
        } catch (IllegalAccessException | InstantiationException e) {
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