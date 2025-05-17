package fii.css.database;

import fii.css.database.persistence.managers.*;

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
    Connection connection;

    public DisciplineManager disciplineManager;
    public TeacherManager teacherManager;
    public RoomManager roomManager;
    public SemiYearManager semiYearManager;
    public FacultyGroupManager facultyGroupManager;
    public ScheduleManager scheduleManager;
    public TeacherDisciplineManager teacherDisciplineManager;

    Database() {}

    public Connection getConnection() { return connection; }

    public void initialize() {
        try {
            this.connection = DriverManager.getConnection(DB_URL);

            // setting auto commit for batch operations
            this.connection.setAutoCommit(false);

            // check if database is empty
            if (isDatabaseEmpty()) {
                System.out.println("Database is empty. Creating tables...");
                String dbPath = "/database_creation.sql";
                executeSqlScript(dbPath); // create the database tables
                this.connection.commit();

                System.out.println("Importing initial data...");
                importInitialData(); // add initial database info

                // commit all data changes
                this.connection.commit();
                System.out.println("All data committed to database successfully");
            } else {
                System.out.println("Database already exists with tables. Skipping creation.");
            }

            // reset auto-commit to true for normal operations
            this.connection.setAutoCommit(true);

            // initialize managers
            initializeManagers();

        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                    System.err.println("Transaction rolled back due to error");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            throw new RuntimeException("Failed to initialize database", e);
        } catch (IOException e) {
            System.err.println("Error importing initial data: " + e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                    System.err.println("Transaction rolled back due to error");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            throw new RuntimeException("Failed to import initial data", e);
        }
    }

    void initializeManagers() {
        disciplineManager = new DisciplineManager();
        teacherManager = new TeacherManager();
        roomManager = new RoomManager();
        semiYearManager = new SemiYearManager();
        facultyGroupManager = new FacultyGroupManager();
        scheduleManager = new ScheduleManager();
        teacherDisciplineManager = new TeacherDisciplineManager();
    }

    boolean isDatabaseEmpty() {
        // query to check if there are any tables in the database
        String checkQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%';";
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery(checkQuery);
            return !rs.next(); // if result set is empty, database has no tables
        } catch (SQLException e) {
            System.err.println("Error checking if database is empty: " + e.getMessage());
            return true; // assume empty if there's an error
        }
    }

    void executeSqlScript(String resourcePath) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath))))) {

            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }

            // split the script into individual statements
            String[] statements = script.toString().split(";\\s*[\r\n]+");

            // execute each statement
            try (Statement stmt = connection.createStatement()) {
                for (String statement : statements) {
                    statement = statement.trim();
                    if (!statement.isEmpty()) {
                        try {
                            stmt.execute(statement);
                        } catch (SQLException e) {
                            System.err.println("Error executing SQL statement: " + statement);
                            throw e;
                        }
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void importInitialData() throws SQLException, IOException {
        var conn = getConnection();

        try {
            // debug to verify resources are being found
            System.out.println("Loading teacher.csv from resources...");
            InputStream teacherStream = getClass().getResourceAsStream("/teacher.csv");
            if (teacherStream == null) {
                System.err.println("WARNING: teacher.csv resource not found!");
            } else {
                teacherStream.close();
            }

            // import data with explicit transaction handling
            CsvDataImporter.importTeachers(conn, "/teacher.csv");
            // force commit after each import to ensure data is saved
            conn.commit();
            System.out.println("Teachers committed to database");

            CsvDataImporter.importTeacherDiscipline(conn, "/teacherdiscipline.csv");
            conn.commit();
            System.out.println("Teacher disciplines committed to database");

            CsvDataImporter.importDisciplines(conn, "/discipline.csv");
            conn.commit();
            System.out.println("Disciplines committed to database");

            CsvDataImporter.importRoom(conn, "/room.csv");
            conn.commit();
            System.out.println("Rooms committed to database");

            CsvDataImporter.importSemiYear(conn, "/semiyear.csv");
            conn.commit();
            System.out.println("Semi-years committed to database");

            CsvDataImporter.importFacultyGroups(conn, "/facultygroup.csv");
            conn.commit();
            System.out.println("Faculty groups committed to database");

        } catch (SQLException | IOException e) {
            System.err.println("Error during data import: " + e.getMessage());
            conn.rollback(); // roll back on any error
            throw e;
        }
    }

    public DisciplineManager getDisciplineManager() {
        return disciplineManager;
    }

    public TeacherManager getTeacherManager() {
        return teacherManager;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public SemiYearManager getSemiYearManager() {
        return semiYearManager;
    }

    public FacultyGroupManager getFacultyGroupManager() {
        return facultyGroupManager;
    }

    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    public TeacherDisciplineManager getTeacherDisciplineManager() {
        return teacherDisciplineManager;
    }
}