package controllers;

import compilation.StringCompilerAndRunner;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.lang.Package;
import java.rmi.server.LoaderHandler;
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

    public static Result getKeywords() {
        Package pkg = Package.getPackage("");
        return ok("keywords OK");
    }
}
