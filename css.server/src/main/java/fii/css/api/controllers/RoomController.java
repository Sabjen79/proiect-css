package fii.css.api.controllers;

import fii.css.api.Query;
import fii.css.database.Database;
import fii.css.database.persistence.managers.RoomManager;
import fii.css.database.persistence.managers.StudyYearManager;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class RoomController extends AbstractController{
    private final RoomManager manager;

    public RoomController() {
        super("rooms");
        manager = Database.getInstance().roomManager;
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
        manager.addRoom(
                Query.stringParam(ctx, "name"),
                Query.integerParam(ctx, "capacity"),
                Query.roomTypeParam(ctx, "roomType")
        );

        ctx.status(201);
    }

    public void update(Context ctx) {
        manager.updateRoom(
                Query.idPathParam(ctx),
                Query.stringParam(ctx, "name"),
                Query.integerParam(ctx, "capacity"),
                Query.roomTypeParam(ctx, "roomType")
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
