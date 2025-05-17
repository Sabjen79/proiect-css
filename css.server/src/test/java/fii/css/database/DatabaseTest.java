package fii.css.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatabaseTest {

    Database db;

    @BeforeEach
    void setUp() {
        db = Database.getInstance();
    }

    @Test
    void testGetInstance() {
        assertSame(db, Database.getInstance());
    }

    @Test
    void testIsDatabaseEmptyWhenEmpty() throws SQLException {
        Connection mockConn = mock(Connection.class);
        Statement mockStmt = mock(Statement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.createStatement()).thenReturn(mockStmt);
        when(mockStmt.executeQuery(anyString())).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        db.connection = mockConn;

        assertTrue(db.isDatabaseEmpty());
    }

    @Test
    void testIsDatabaseEmptyWhenNotEmpty() throws SQLException {
        Connection mockConn = mock(Connection.class);
        Statement mockStmt = mock(Statement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.createStatement()).thenReturn(mockStmt);
        when(mockStmt.executeQuery(anyString())).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);

        db.connection = mockConn;

        assertFalse(db.isDatabaseEmpty());
    }

    @Test
    void testExecuteSqlScriptExecutesStatements() throws Exception {
        Connection mockConn = mock(Connection.class);
        Statement mockStmt = mock(Statement.class);

        when(mockConn.createStatement()).thenReturn(mockStmt);
        db.connection = mockConn;

        // Place a test.sql in src/test/resources with a dummy statement for this to run
        db.executeSqlScript("/test.sql");

        verify(mockStmt, atLeastOnce()).execute(anyString());
    }


}
