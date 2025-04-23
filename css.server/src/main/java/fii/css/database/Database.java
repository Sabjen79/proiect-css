package fii.css.database;

import fii.css.database.persistence.managers.TeacherManager;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:database.db";

    private static final Database INSTANCE = new Database();

    public static Database getInstance() {
        return INSTANCE;
    }

    //======================================================================//
    private Connection connection;

    public TeacherManager teacherManager;

    private Database() {}

    public Connection getConnection() { return connection; }

    public void initialize() {
        try {
            this.connection = DriverManager.getConnection(DB_URL);
            if (isDatabaseEmpty()) {
                System.out.println("Database does not exist. Running creation script...");
                String dbPath = "/database_creation.sql";
                executeSqlScript(dbPath); // create the database
            } else {
                System.out.println("Database already exists. Skipping creation.");
            }

            teacherManager = new TeacherManager();
        } catch (SQLException e) {
            // if the database doesn't exist yet
            System.out.println("Database file does not exist. Creating new database...");
            String dbPath = "/database_creation.sql";
            executeSqlScript(dbPath);
        }
    }

    private boolean isDatabaseEmpty() {
        // query to check if there are any tables in the database
        String checkQuery = "SELECT name FROM sqlite_master WHERE type='table';";
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery(checkQuery);
            return !rs.next();
        } catch (SQLException e) {
            // if there's an error (e.g., the database file doesn't exist), assume the database is empty
            return true;
        }
    }


    private void executeSqlScript(String resourcePath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath))))) {

            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }

            String[] statements = script.toString().split("(?m);\\s*\n"); // split on semicolon followed by newline

            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    try (Statement stmt = connection.createStatement()) {
                        stmt.execute(statement);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
