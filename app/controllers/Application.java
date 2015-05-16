package controllers;

import play.mvc.Result;
import views.html.index;

import static play.mvc.Results.ok;

public class Application {

    public static Result index() {
        return ok(index.render("Let's get working guys !"));
    }

    /**
     * Call to compilation service
     *
     * @return result compilation result
     */
    public static Result compile() {
        return ok("compilation OK");
    }
}
