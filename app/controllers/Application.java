package controllers;

import compilation.StringCompilerAndRunner;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

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
        String code = "package compilation;\n" +
                "public class ChocoProjectImpl implements ChocoProject {" + "\n"
                + "public void init() {" + "\n"
                + "System.out.println(\"... init du code compilé ...\");" + "\n"
                + "}" + "\n"
                + "public void run() {" + "\n"
                + "System.out.println(\"... run du code ...\");" + "\n"
                + "}" + "\n"
                + "}" + "\n";
        System.out.println(code);
        compileAndRunFromString(code);
        return ok("compilation OK");
    }

    private static void compileAndRunFromString(String code) throws InstantiationException, IllegalAccessException {
        StringCompilerAndRunner compilerAndRunner = new StringCompilerAndRunner();
        compilerAndRunner.compileAndRun(code);
    }
}
