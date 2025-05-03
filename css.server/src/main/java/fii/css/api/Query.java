package fii.css.api;

import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.ClassType;
import fii.css.database.persistence.entities.DayOfWeek;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.RoomType;
import io.javalin.http.Context;

public class Query {
    public static String idPathParam(Context ctx) {
        return ctx.pathParam("id");
    }

    public static Degree degreeParam(Context ctx, String value) {
        return Degree.fromValue(integerParam(ctx, value));
    }

    public static ClassType classTypeParam(Context ctx, String value) {
        return ClassType.fromValue(integerParam(ctx, value));
    }

    public static DayOfWeek dayOfWeekParam(Context ctx, String value) {
        return DayOfWeek.fromValue(integerParam(ctx, value));
    }

    public static RoomType roomTypeParam(Context ctx, String value) {
        return RoomType.fromValue(integerParam(ctx, value));
    }

    public static Integer integerParam(Context ctx, String name) {
        var param = ctx.queryParam(name);

        if (param == null) {
            throw new DatabaseException("Query parameter '" + name + "' not found");
        }

        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            throw new DatabaseException("'" + name + "' should be an integer");
        }
    }

    public static String stringParam(Context ctx, String name) {
        var param = ctx.queryParam(name);

        if (param == null) {
            throw new DatabaseException("Query parameter '" + name + "' not found");
        }

        if (param.isEmpty()) {
            throw new DatabaseException("'" + name + "' cannot be empty");
        }

        return param;
    }
}
