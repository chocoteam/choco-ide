package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import compilation.StringCompilerAndRunner;
import datas.samples.Sample;
import datas.samples.SampleManager;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.Collection;
import java.util.List;
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

    public static Result getCodeSampleList() throws JsonProcessingException {
        Collection<Sample> availableSample = SampleManager.getInstance().getAvailableSample();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(availableSample);
        return ok(json);
    }
}