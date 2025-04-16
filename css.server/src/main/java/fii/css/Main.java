package fii.css;

import fii.css.api.RestApi;
import fii.css.database.Database;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database database = Database.getInstance();
        database.initialize();

        // TODO: Delete this example later
        var teacherManager = database.teacherManager;

        var teacher = teacherManager.addTeacher("Andrei Andrei", "Assistant");

        teacher = teacherManager.updateTeacher(teacher.getTeacherId(), "Andrei Andrei", "Bo$$");

        teacherManager.removeTeacher(teacher.getTeacherId());

        RestApi api = new RestApi();
        api.start();
    }
}