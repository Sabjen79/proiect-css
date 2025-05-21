package fii.css.api.controllers;

import fii.css.api.Query;
import fii.css.database.Database;
import fii.css.database.persistence.managers.ScheduleManager;
import fii.css.database.persistence.managers.TeacherManager;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Arrays;

public class ScheduleController extends AbstractController {
    private final ScheduleManager manager;

    public ScheduleController() {
        super("schedules");
        manager = Database.getInstance().getScheduleManager();
    }

    @Override
    public void registerEndpoints(Javalin app) {
        app.get(path + "/{id}", this::get);
        app.get(path, this::getAll);
        app.post(path, this::create);
        app.patch(path + "/{id}", this::update);
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
        manager.addSchedule(
                Query.stringParam(ctx, "teacherId"),
                Query.stringParam(ctx, "disciplineId"),
                Query.classTypeParam(ctx, "classType"),
                Query.stringParam(ctx, "roomId"),
                Query.stringParam(ctx, "studentsId"),
                Query.dayOfWeekParam(ctx, "dayOfWeek"),
                Query.integerParam(ctx, "startHour")
        );

        ctx.status(201);
    }

    public void update(Context ctx) {
        manager.updateSchedule(
                Query.idPathParam(ctx),
                Query.stringParam(ctx, "teacherId"),
                Query.stringParam(ctx, "disciplineId"),
                Query.classTypeParam(ctx, "classType"),
                Query.stringParam(ctx, "roomId"),
                Query.stringParam(ctx, "studentsId"),
                Query.dayOfWeekParam(ctx, "dayOfWeek"),
                Query.integerParam(ctx, "startHour")
        );

        ctx.status(204);
    }

    public void delete(Context ctx) {
        manager.remove(
                Query.idPathParam(ctx)
        );

        ctx.status(204);
    }
}
