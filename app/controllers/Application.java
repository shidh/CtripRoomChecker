package controllers;

import play.Logger;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {

        Logger.info("A log message");
        render();
    }

}