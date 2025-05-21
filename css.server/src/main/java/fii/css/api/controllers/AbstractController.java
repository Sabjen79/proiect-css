package fii.css.api.controllers;

import io.javalin.Javalin;

public abstract class AbstractController {
    public String path;

    protected AbstractController(String path) {
        this.path = "/" + path;
    }

    public abstract void registerEndpoints(Javalin app);
}
