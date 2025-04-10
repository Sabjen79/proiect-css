package fii.css.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    static final String DB_URL = "jdbc:sqlite:database.db";

    public static final Database INSTANCE = new Database();

    //======================================================================//

    private Database() {}

    public void initialize() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        Statement statement = connection.createStatement();
    }
}
