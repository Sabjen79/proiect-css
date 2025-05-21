package fii.css.database;

import fii.css.database.persistence.managers.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatabaseTest {
    private Database db;

    @BeforeEach
    void setup() {
        db = Database.getInstance();
    }

    @Test
    void testGetInstanceReturnsSingleton() {
        Database db2 = Database.getInstance();
        assertSame(db, db2);
    }

    @Test
    void testIsDatabaseEmptyTrue() throws SQLException {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        db.connection = conn;
        assertTrue(db.isDatabaseEmpty());
    }

    @Test
    void testIsDatabaseEmptyFalse() throws SQLException {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        db.connection = conn;
        assertFalse(db.isDatabaseEmpty());
    }

    @Test
    void testExecuteSqlScriptRunsStatements() throws Exception {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);

        when(conn.createStatement()).thenReturn(stmt);
        db.connection = conn;

        InputStream stream = new ByteArrayInputStream("CREATE TABLE test (id INTEGER);".getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try (MockedStatic<Database> dbMock = mockStatic(Database.class)) {
            when(Database.getInstance()).thenReturn(db);
            db.executeSqlScript("/test.sql");
            verify(stmt, atLeastOnce()).execute(anyString());
        }
    }

    @Test
    void testImportInitialDataCommitsAfterEachImport() throws Exception {
        Database dbSpy = spy(Database.getInstance());
        Connection conn = mock(Connection.class);

        doReturn(conn).when(dbSpy).getConnection();
        doNothing().when(conn).commit();

        try (
                MockedStatic<Database> dbMock = mockStatic(Database.class);
                MockedStatic<CsvDataImporter> importer = mockStatic(CsvDataImporter.class)
        ) {
            dbMock.when(Database::getInstance).thenReturn(dbSpy);

            importer.when(() -> CsvDataImporter.importTeachers(any(), anyString())).then(invocation -> null);
            importer.when(() -> CsvDataImporter.importTeacherDiscipline(any(), anyString())).then(invocation -> null);
            importer.when(() -> CsvDataImporter.importDisciplines(any(), anyString())).then(invocation -> null);
            importer.when(() -> CsvDataImporter.importRoom(any(), anyString())).then(invocation -> null);
            importer.when(() -> CsvDataImporter.importSemiYear(any(), anyString())).then(invocation -> null);
            importer.when(() -> CsvDataImporter.importFacultyGroups(any(), anyString())).then(invocation -> null);

            dbSpy.importInitialData();

            verify(conn, times(6)).commit();
        }
    }

    @Test
    void testInitializeDatabaseFlowWhenEmpty() throws Exception {
        Database dbSpy = spy(Database.getInstance());
        Connection conn = mock(Connection.class);

        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
            driverManagerMock.when(() -> DriverManager.getConnection(anyString()))
                    .thenReturn(conn);

            doReturn(true).when(dbSpy).isDatabaseEmpty();
            doNothing().when(dbSpy).executeSqlScript(anyString());
            doNothing().when(dbSpy).importInitialData();
            doNothing().when(dbSpy).initializeManagers();
            doNothing().when(conn).setAutoCommit(anyBoolean());
            doNothing().when(conn).commit();

            dbSpy.initialize();

            verify(dbSpy).executeSqlScript("/database_creation.sql");
            verify(dbSpy).importInitialData();
            verify(conn, times(2)).commit();
            verify(conn).setAutoCommit(true);
        }
    }

    @Test
    void testInitializeDatabaseFlowWhenNotEmpty() throws Exception {
        Database dbSpy = spy(new Database());
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.execute(anyString())).thenReturn(true);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
            driverManagerMock.when(() -> DriverManager.getConnection(anyString()))
                    .thenReturn(conn);

            doReturn(false).when(dbSpy).isDatabaseEmpty();
            doNothing().when(dbSpy).initializeManagers();

            dbSpy.initialize();

            verify(conn).setAutoCommit(true);
            verify(dbSpy, never()).executeSqlScript(anyString());
        }
    }

    @Test
    void testRollbackOnSQLException() throws Exception {
        Database dbSpy = spy(new Database());
        Connection conn = mock(Connection.class);

        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
            driverManagerMock.when(() -> DriverManager.getConnection(anyString()))
                    .thenReturn(conn);

            doReturn(true).when(dbSpy).isDatabaseEmpty();
            doNothing().when(dbSpy).executeSqlScript(anyString());
            doThrow(new SQLException("Simulated SQL error"))
                    .when(dbSpy).importInitialData();
            doNothing().when(conn).rollback();
            assertThrows(RuntimeException.class, dbSpy::initialize);
            verify(conn).rollback();
        }
    }

    @Test
    void testRollbackOnIOException() throws Exception {
        Database dbSpy = spy(new Database());
        Connection conn = mock(Connection.class);

        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
            driverManagerMock.when(() -> DriverManager.getConnection(anyString()))
                    .thenReturn(conn);

            doReturn(true).when(dbSpy).isDatabaseEmpty();
            doNothing().when(dbSpy).executeSqlScript(anyString());
            doThrow(new IOException("Simulated IO error")).when(dbSpy).importInitialData();
            doNothing().when(conn).setAutoCommit(anyBoolean());
            doNothing().when(conn).rollback();

            assertThrows(RuntimeException.class, dbSpy::initialize);
            verify(conn).rollback();
        }
    }

    @Test
    void testGetDisciplineManagerReturnsInstance() {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);

        try {
            when(conn.createStatement()).thenReturn(stmt);
            when(stmt.executeQuery(anyString())).thenReturn(rs);
            when(rs.next()).thenReturn(false); // pretend no data

            db.connection = conn;
            db.initializeManagers();

            assertNotNull(db.getDisciplineManager());
            assertTrue(db.getDisciplineManager() instanceof DisciplineManager);
        } catch (SQLException e) {
            fail("SQLException should not be thrown");
        }
    }


    @Test
    void testGetTeacherManagerReturnsInstance() {
        db.initializeManagers();
        assertNotNull(db.getTeacherManager());
        assertTrue(db.getTeacherManager() instanceof TeacherManager);
    }

    @Test
    void testGetRoomManagerReturnsInstance() {
        db.initializeManagers();
        assertNotNull(db.getRoomManager());
        assertTrue(db.getRoomManager() instanceof RoomManager);
    }

    @Test
    void testGetSemiYearManagerReturnsInstance() {
        db.initializeManagers();
        assertNotNull(db.getSemiYearManager());
        assertTrue(db.getSemiYearManager() instanceof SemiYearManager);
    }

    @Test
    void testGetFacultyGroupManagerReturnsInstance() {
        db.initializeManagers();
        assertNotNull(db.getFacultyGroupManager());
        assertTrue(db.getFacultyGroupManager() instanceof FacultyGroupManager);
    }

    @Test
    void testGetScheduleManagerReturnsInstance() {
        db.initializeManagers();
        assertNotNull(db.getScheduleManager());
        assertTrue(db.getScheduleManager() instanceof ScheduleManager);
    }

    @Test
    void testGetTeacherDisciplineManagerReturnsInstance() {
        db.initializeManagers();
        assertNotNull(db.getTeacherDisciplineManager());
        assertTrue(db.getTeacherDisciplineManager() instanceof TeacherDisciplineManager);
    }
}
