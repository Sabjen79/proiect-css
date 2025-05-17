
package fii.css.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CsvDataImporterTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false); // Return true once, then false
        when(mockResultSet.getInt(1)).thenReturn(3); // Mock count result
    }

    @Test
    void testParseCsvLine() {
        String[] result = CsvDataImporter.parseCsvLine("id,name,title");
        assertArrayEquals(new String[]{"id", "name", "title"}, result);

        // Test with quoted values
        result = CsvDataImporter.parseCsvLine("id,\"Smith, John\",\"Professor\"");
        assertArrayEquals(new String[]{"id", "Smith, John", "Professor"}, result);

        // Test with empty fields
        result = CsvDataImporter.parseCsvLine("id,,title");
        assertArrayEquals(new String[]{"id", "", "title"}, result);
    }

    @Test
    void testImportTeachers() throws Exception {
        String csvContent = "id,name,title\nT1,John Smith,Professor\nT2,Jane Doe,Assistant";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        CsvDataImporter.importTeachers(mockConnection, inputStream);

        verify(mockConnection).prepareStatement("INSERT INTO Teacher (id, name, title) VALUES (?, ?, ?)");

        verify(mockPreparedStatement).setString(1, "T1");
        verify(mockPreparedStatement).setString(2, "John Smith");
        verify(mockPreparedStatement).setString(3, "Professor");

        verify(mockPreparedStatement).setString(1, "T2");
        verify(mockPreparedStatement).setString(2, "Jane Doe");
        verify(mockPreparedStatement).setString(3, "Assistant");

        verify(mockPreparedStatement, times(2)).executeUpdate();

        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM Teacher");
    }

    @Test
    void testImportTeacherDiscipline() throws Exception {
        String csvContent = "id,teacher_id,discipline_id\nTD1,T1,D1\nTD2,T2,D2";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        CsvDataImporter.importTeacherDiscipline(mockConnection, inputStream);

        verify(mockConnection).prepareStatement("INSERT INTO TeacherDiscipline (id, teacher_id, discipline_id) VALUES (?, ?, ?)");

        verify(mockPreparedStatement).setString(1, "TD1");
        verify(mockPreparedStatement).setString(2, "T1");
        verify(mockPreparedStatement).setString(3, "D1");

        verify(mockPreparedStatement).setString(1, "TD2");
        verify(mockPreparedStatement).setString(2, "T2");
        verify(mockPreparedStatement).setString(3, "D2");

        verify(mockPreparedStatement, times(2)).executeUpdate();

        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM TeacherDiscipline");
    }

    @Test
    void testImportDisciplines() throws Exception {
        String csvContent = "id,name,degree,year\nD1,Math,Bachelor,1\nD2,Physics,Master,2";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        CsvDataImporter.importDisciplines(mockConnection, inputStream);

        verify(mockConnection).prepareStatement("INSERT INTO Discipline (id, name, degree, year) VALUES (?, ?, ?, ?)");

        verify(mockPreparedStatement).setString(1, "D1");
        verify(mockPreparedStatement).setString(2, "Math");
        verify(mockPreparedStatement).setInt(3, 0); // Bachelor -> 0
        verify(mockPreparedStatement).setInt(4, 1);

        verify(mockPreparedStatement).setString(1, "D2");
        verify(mockPreparedStatement).setString(2, "Physics");
        verify(mockPreparedStatement).setInt(3, 1); // Master -> 1
        verify(mockPreparedStatement).setInt(4, 2);

        verify(mockPreparedStatement, times(2)).executeUpdate();

        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM Discipline");
    }

    @Test
    void testImportDisciplinesWithInvalidDegree() {
        String csvContent = "id,name,degree,year\nD1,Math,Invalid,1";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CsvDataImporter.importDisciplines(mockConnection, inputStream);
        });

        assertTrue(exception.getMessage().contains("Unknown degree value: Invalid"));
    }

    @Test
    void testImportRoom() throws Exception {
        String csvContent = "id,name,capacity,room_type\nR1,Room 101,30,Course\nR2,Lab 202,20,Laboratory";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        CsvDataImporter.importRoom(mockConnection, inputStream);


        verify(mockConnection).prepareStatement("INSERT INTO Room (id, name, capacity, room_type) VALUES (?, ?, ?, ?)");

        verify(mockPreparedStatement).setString(1, "R1");
        verify(mockPreparedStatement).setString(2, "Room 101");
        verify(mockPreparedStatement).setInt(3, 30);
        verify(mockPreparedStatement).setInt(4, 0); // Course -> 0

        verify(mockPreparedStatement).setString(1, "R2");
        verify(mockPreparedStatement).setString(2, "Lab 202");
        verify(mockPreparedStatement).setInt(3, 20);
        verify(mockPreparedStatement).setInt(4, 1); // Laboratory -> 1

        verify(mockPreparedStatement, times(2)).executeUpdate();

        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM Room");
    }

    @Test
    void testImportRoomAllTypes() throws Exception {
        String[] roomTypes = {"Course", "Laboratory", "Seminary", "Office", "Equipment", "Unknown"};
        int[] expectedValues = {0, 1, 2, 3, 4, -1};

        for (int i = 0; i < roomTypes.length; i++) {
            // reset mocks
            Mockito.reset(mockConnection, mockPreparedStatement, mockResultSet);

            // set up mocks again
            PreparedStatement insertStatement = mock(PreparedStatement.class);
            PreparedStatement countStatement = mock(PreparedStatement.class);
            ResultSet countResultSet = mock(ResultSet.class);

            // configure mock behavior
            when(mockConnection.prepareStatement("INSERT INTO Room (id, name, capacity, room_type) VALUES (?, ?, ?, ?)"))
                    .thenReturn(insertStatement);
            when(mockConnection.prepareStatement("SELECT COUNT(*) FROM Room"))
                    .thenReturn(countStatement);
            when(countStatement.executeQuery()).thenReturn(countResultSet);
            when(countResultSet.next()).thenReturn(true, false);
            when(countResultSet.getInt(1)).thenReturn(1);

            String csvContent = "id,name,capacity,room_type\nR1,Test Room,10," + roomTypes[i];
            InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
            CsvDataImporter.importRoom(mockConnection, inputStream);

            verify(insertStatement).setString(1, "R1");
            verify(insertStatement).setString(2, "Test Room");
            verify(insertStatement).setInt(3, 10);
            verify(insertStatement).setInt(4, expectedValues[i]);
            verify(insertStatement).executeUpdate();
        }
    }

    @Test
    void testImportSemiYear() throws Exception {
        String csvContent = "id,name,degree,year\nS1,Sem1,Bachelor,1\nS2,Sem2,Master,2";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        CsvDataImporter.importSemiYear(mockConnection, inputStream);

        verify(mockConnection).prepareStatement("INSERT INTO SemiYear (id, name, degree, year) VALUES (?, ?, ?, ?)");

        verify(mockPreparedStatement).setString(1, "S1");
        verify(mockPreparedStatement).setString(2, "Sem1");
        verify(mockPreparedStatement).setInt(3, 0); // Bachelor -> 0
        verify(mockPreparedStatement).setInt(4, 1);

        verify(mockPreparedStatement).setString(1, "S2");
        verify(mockPreparedStatement).setString(2, "Sem2");
        verify(mockPreparedStatement).setInt(3, 1); // Master -> 1
        verify(mockPreparedStatement).setInt(4, 2);

        verify(mockPreparedStatement, times(2)).executeUpdate();

        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM SemiYear");
    }

    @Test
    void testImportSemiYearWithInvalidDegree() {
        String csvContent = "id,name,degree,year\nS1,Sem1,Invalid,1";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CsvDataImporter.importSemiYear(mockConnection, inputStream);
        });

        assertTrue(exception.getMessage().contains("Unknown degree value: Invalid"));
    }

    @Test
    void testImportFacultyGroups() throws Exception {
        String csvContent = "id,name,semi_year_id\nFG1,Group A,S1\nFG2,Group B,S2";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        CsvDataImporter.importFacultyGroups(mockConnection, inputStream);

        verify(mockConnection).prepareStatement("INSERT INTO FacultyGroup (id, name, semi_year_id) VALUES (?, ?, ?)");

        verify(mockPreparedStatement).setString(1, "FG1");
        verify(mockPreparedStatement).setString(2, "Group A");
        verify(mockPreparedStatement).setString(3, "S1");

        verify(mockPreparedStatement).setString(1, "FG2");
        verify(mockPreparedStatement).setString(2, "Group B");
        verify(mockPreparedStatement).setString(3, "S2");

        verify(mockPreparedStatement, times(2)).executeUpdate();

        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM FacultyGroup");
    }

    @Test
    void testImportFromFileNotFound() {
        String nonExistentFile = "/non/existent/file.csv";

        assertThrows(FileNotFoundException.class, () -> {
            CsvDataImporter.importTeachers(mockConnection, nonExistentFile);
        });

        assertThrows(FileNotFoundException.class, () -> {
            CsvDataImporter.importTeacherDiscipline(mockConnection, nonExistentFile);
        });

        assertThrows(FileNotFoundException.class, () -> {
            CsvDataImporter.importDisciplines(mockConnection, nonExistentFile);
        });

        assertThrows(FileNotFoundException.class, () -> {
            CsvDataImporter.importRoom(mockConnection, nonExistentFile);
        });

        assertThrows(FileNotFoundException.class, () -> {
            CsvDataImporter.importSemiYear(mockConnection, nonExistentFile);
        });

        assertThrows(FileNotFoundException.class, () -> {
            CsvDataImporter.importFacultyGroups(mockConnection, nonExistentFile);
        });
    }

    @Test
    void testEmptyCsv() throws Exception {
        String csvContent = "id,name,title";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        CsvDataImporter.importTeachers(mockConnection, inputStream);

        verify(mockPreparedStatement, never()).executeUpdate();

        verify(mockConnection).prepareStatement("SELECT COUNT(*) FROM Teacher");
    }
}