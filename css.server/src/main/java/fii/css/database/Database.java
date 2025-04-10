package fii.css.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:database.db";

    private static final Database INSTANCE = new Database();
    public static Database getInstance() { return INSTANCE; }

    //======================================================================//
    private Connection connection;

    private Database() {}

    public void initialize() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL);
    }
}
