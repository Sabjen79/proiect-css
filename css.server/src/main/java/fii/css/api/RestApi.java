package fii.css.api;

import fii.css.api.controllers.*;
import fii.css.database.DatabaseException;
import io.javalin.Javalin;

/// The REST API server responsible for communicating with the UI.
/// Its purpose is to expose methods from the Database object, NOT to implement logic.
public class RestApi {
    private final Javalin app;

    public RestApi() {
        app = Javalin.create();

        registerController(StudyYearController.class);
        registerController(FacultyGroupController.class);
        registerController(RoomController.class);
        registerController(DisciplineController.class);
        registerController(TeacherController.class);
        registerController(ScheduleController.class);

        app.exception(DatabaseException.class, (e, ctx) -> {
            ctx.result(e.getMessage()).status(400);
        });
        app.exception(Exception.class, (e, ctx) -> {
            ctx.result(e.getMessage()).status(500);
        });
    }

    public void start() {
        app.start(8080);
    }

    private void registerController(Class<? extends AbstractController> controllerClass) {
        try {
            var controller = controllerClass.getDeclaredConstructor().newInstance();

            controller.registerEndpoints(app);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
