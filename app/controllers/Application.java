package controllers;

import play.mvc.*;
import views.html.index;
import views.html.main;

import static play.mvc.Results.ok;

/**
 * Created by dev on 13/05/15.
 */
public class Application {

    public static Result index () {
        return ok(index.render("Let's get working guys !"));
    }

}
