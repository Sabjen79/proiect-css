package fii.css.api.controllers;

import fii.css.api.Query;
import fii.css.database.Database;
import fii.css.database.persistence.managers.TeacherDisciplineManager;
import fii.css.database.persistence.managers.TeacherManager;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class TeacherDisciplineController extends AbstractController {
    private final TeacherDisciplineManager manager;

    public TeacherDisciplineController() {
        super("teacherDisciplines");
        manager = Database.getInstance().teacherDisciplineManager;
    }

    @Override
    public void registerEndpoints(Javalin app) {
        app.get(path + "/{id}", this::get);
        app.get(path, this::getAll);
        app.post(path, this::create);
        app.delete(path + "/{id}", this::delete);
    }

    public void get(Context ctx) {
        var obj = manager.get(ctx.pathParam("id"));

        if(obj == null) {
            ctx.status(404);
            return;
        }

        ctx.json(obj).status(200);
    }

    public void getAll(Context ctx) {
        var list = manager.getAll();

        ctx.json(list).status(200);
    }

    public void create(Context ctx) {
        manager.addTeacherDiscipline(
                Query.stringParam(ctx, "teacher_id"),
                Query.stringParam(ctx, "discipline_id")
        );

        ctx.status(201);
    }

    public void delete(Context ctx) {
        manager.remove(
                Query.idPathParam(ctx)
        );

        ctx.status(204);
    }
}