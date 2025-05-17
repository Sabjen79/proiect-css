package fii.css.database;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    Database db;

    @BeforeEach
    void setup() throws SQLException {
        db = Database.getInstance();
        db.connection = DriverManager.getConnection("jdbc:sqlite:file:memdb1?mode=memory&cache=shared\n");
        db.connection.setAutoCommit(true);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (db.connection != null && !db.connection.isClosed()) {
            db.connection.close();
        }
    }

    @Test
    void testGetInstanceReturnsSameObject() {
        Database db2 = Database.getInstance();
        assertSame(db, db2, "getInstance() should return the singleton instance");
    }

    @Test
    void testIsDatabaseEmptyTrueWhenNoTables() throws SQLException {
        assertTrue(db.isDatabaseEmpty(), "Database should be empty when no tables exist");
    }

    @Test
    void testIsDatabaseEmptyFalseWhenTablesExist() throws SQLException {
        try (Statement stmt = db.connection.createStatement()) {
            stmt.execute("CREATE TABLE test_table (id INTEGER PRIMARY KEY);");
        }
        assertFalse(db.isDatabaseEmpty(), "Database should not be empty when a table exists");
    }

    @Test
    void testExecuteSqlScriptRunsStatementsFromResource() throws Exception {
        // Assumes src/test/resources/test.sql contains: CREATE TABLE test_table (id INTEGER PRIMARY KEY, name TEXT);
        db.executeSqlScript("/test.sql");

        try (Statement stmt = db.connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='Discipline';"
            );
            assertTrue(rs.next(), "Table 'Discipline' should have been created");
        }
    }

}
