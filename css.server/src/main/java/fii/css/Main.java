package fii.css;

import fii.css.api.RestApi;
import fii.css.database.Database;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database database = Database.getInstance();
        database.initialize();

        RestApi api = new RestApi();
        api.start();
    }
}
