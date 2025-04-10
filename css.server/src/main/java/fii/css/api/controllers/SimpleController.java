package fii.css.api.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class SimpleController extends AbstractController {
    public SimpleController() {
        super("simple");
    }

    @Override
    public void registerEndpoints(Javalin app) {
        app.get(path + "/get", this::simpleEndpoint);
    }

    public void simpleEndpoint(Context ctx) {
        ctx.status(200).result("Salve");
    }
}
