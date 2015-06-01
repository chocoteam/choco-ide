package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import compilation.StringCompilerAndRunner;
import datas.keywords.KeywordsManager;
import datas.samples.Sample;
import datas.samples.SampleManager;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.Package;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.rmi.server.LoaderHandler;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

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
        List<Sample> availableSample = SampleManager.getInstance().getAvailableSample();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(availableSample);
        return ok(json);
    }

    public static Result getKeywords() throws IOException, ClassNotFoundException {
        String chocoClasses = KeywordsManager.getChocoClassesName();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(chocoClasses);
        return ok(json);
    }

}
