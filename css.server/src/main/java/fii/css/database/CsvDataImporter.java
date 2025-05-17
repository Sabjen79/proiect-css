package fii.css.database;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CsvDataImporter {

    public static void importTeachers(Connection conn, InputStream inputStream) throws IOException, SQLException {
        String sql = "INSERT INTO Teacher (id, name, title) VALUES (?, ?, ?)";
        List<String[]> records = readCsvFile(inputStream);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] record : records) {
                pstmt.setString(1, record[0]);
                pstmt.setString(2, record[1]);
                pstmt.setString(3, record[2]);
                pstmt.executeUpdate();
            }
        }
        System.out.println("Teachers imported successfully");

        verifyImport(conn, "Teacher");
    }

    public static void importTeacherDiscipline(Connection conn, InputStream inputStream) throws IOException, SQLException {
        String sql = "INSERT INTO TeacherDiscipline (id, teacher_id, discipline_id) VALUES (?, ?, ?)";
        List<String[]> records = readCsvFile(inputStream);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] record : records) {
                pstmt.setString(1, record[0]);
                pstmt.setString(2, record[1]);
                pstmt.setString(3, record[2]);
                pstmt.executeUpdate();
            }
        }
        System.out.println("Teacher discipline imported successfully");

        verifyImport(conn, "TeacherDiscipline");
    }

    public static void importDisciplines(Connection conn, InputStream inputStream) throws IOException, SQLException {
        String sql = "INSERT INTO Discipline (id, name, degree, year) VALUES (?, ?, ?, ?)";
        List<String[]> records = readCsvFile(inputStream);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] record : records) {
                pstmt.setString(1, record[0]);
                pstmt.setString(2, record[1]);

                // convert degree string to numeric value: Bachelor -> 0, Master -> 1
                int degreeValue;
                if (record[2].equalsIgnoreCase("Bachelor")) {
                    degreeValue = 0;
                } else if (record[2].equalsIgnoreCase("Master")) {
                    degreeValue = 1;
                } else {
                    // default or error case
                    throw new IllegalArgumentException("Unknown degree value: " + record[2]);
                }

                pstmt.setInt(3, degreeValue);
                pstmt.setInt(4, Integer.parseInt(record[3]));
                pstmt.executeUpdate();
            }
        }
        System.out.println("Disciplines imported successfully");

        verifyImport(conn, "Discipline");
    }

    public static void importRoom(Connection conn, InputStream inputStream) throws IOException, SQLException {
        String sql = "INSERT INTO Room (id, name, capacity, room_type) VALUES (?, ?, ?, ?)";
        List<String[]> records = readCsvFile(inputStream);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] record : records) {
                pstmt.setString(1, record[0]);
                pstmt.setString(2, record[1]);
                pstmt.setInt(3, Integer.parseInt(record[2]));

                int roomTypeValue = switch (record[3].toLowerCase()) {
                    case "course" -> 0;
                    case "laboratory" -> 1;
                    case "seminary" -> 2;
                    case "office" -> 3;
                    case "equipment" -> 4;
                    default -> -1;
                };

                pstmt.setInt(4, roomTypeValue);
                pstmt.executeUpdate();
            }
        }
        System.out.println("Room imported successfully");

        verifyImport(conn, "Room");
    }

    public static void importSemiYear(Connection conn, InputStream inputStream) throws IOException, SQLException {
        String sql = "INSERT INTO SemiYear (id, name, degree, year) VALUES (?, ?, ?, ?)";
        List<String[]> records = readCsvFile(inputStream);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] record : records) {
                pstmt.setString(1, record[0]);
                pstmt.setString(2, record[1]);

                int degreeValue;
                if (record[2].equalsIgnoreCase("Bachelor")) {
                    degreeValue = 0;
                } else if (record[2].equalsIgnoreCase("Master")) {
                    degreeValue = 1;
                } else {
                    throw new IllegalArgumentException("Unknown degree value: " + record[2]);
                }
                pstmt.setInt(3, degreeValue);
                pstmt.setInt(4, Integer.parseInt(record[3]));
                pstmt.executeUpdate();
            }
        }
        System.out.println("Semi-year imported successfully");

        verifyImport(conn, "SemiYear");
    }

    public static void importFacultyGroups(Connection conn, InputStream inputStream) throws IOException, SQLException {
        String sql = "INSERT INTO FacultyGroup (id, name, semi_year_id) VALUES (?, ?, ?)";
        List<String[]> records = readCsvFile(inputStream);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] record : records) {
                pstmt.setString(1, record[0]);
                pstmt.setString(2, record[1]);
                pstmt.setString(3, record[2]);
                pstmt.executeUpdate();
            }
        }
        System.out.println("Faculty groups imported successfully");

        verifyImport(conn, "FacultyGroup");
    }

    // helper method to verify import by counting records
    private static void verifyImport(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Verified " + count + " records in table " + tableName);
                if (count == 0) {
                    System.err.println("WARNING: Table " + tableName + " has 0 records after import!");
                }
            }
        }
    }

    // helper method to read CSV data from InputStream
    private static List<String[]> readCsvFile(InputStream inputStream) throws IOException {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                // skip header row if it exists
                if (firstLine) {
                    firstLine = false;
                    // check if this is a header row
                    if (line.startsWith("id,")) {
                        continue;
                    }
                }

                // split the CSV line, handling quoted fields if necessary
                String[] fields = parseCsvLine(line);
                records.add(fields);
            }
        }

        System.out.println("Read " + records.size() + " records from CSV");
        return records;
    }

    // helper method to properly parse CSV lines (handles quoted fields)
    static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString().trim());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        // add the last field
        fields.add(currentField.toString().trim());

        return fields.toArray(new String[0]);
    }

    public static void importTeachers(Connection conn, String filePath) throws IOException, SQLException {
        try (InputStream is = CsvDataImporter.class.getResourceAsStream(filePath)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }
            importTeachers(conn, is);
        }
    }

    public static void importTeacherDiscipline(Connection conn, String filePath) throws IOException, SQLException {
        try (InputStream is = CsvDataImporter.class.getResourceAsStream(filePath)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }
            importTeacherDiscipline(conn, is);
        }
    }

    public static void importDisciplines(Connection conn, String filePath) throws IOException, SQLException {
        try (InputStream is = CsvDataImporter.class.getResourceAsStream(filePath)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }
            importDisciplines(conn, is);
        }
    }

    public static void importRoom(Connection conn, String filePath) throws IOException, SQLException {
        try (InputStream is = CsvDataImporter.class.getResourceAsStream(filePath)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }
            importRoom(conn, is);
        }
    }

    public static void importSemiYear(Connection conn, String filePath) throws IOException, SQLException {
        try (InputStream is = CsvDataImporter.class.getResourceAsStream(filePath)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }
            importSemiYear(conn, is);
        }
    }

    public static void importFacultyGroups(Connection conn, String filePath) throws IOException, SQLException {
        try (InputStream is = CsvDataImporter.class.getResourceAsStream(filePath)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }
            importFacultyGroups(conn, is);
        }
    }
}