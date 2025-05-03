package fii.css;

import fii.css.api.RestApi;
import fii.css.database.Database;
import fii.css.database.persistence.entities.ClassType;
import fii.css.database.persistence.entities.DayOfWeek;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.RoomType;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database database = Database.getInstance();
        database.initialize();

        RestApi api = new RestApi();
        api.start();
    }
}
