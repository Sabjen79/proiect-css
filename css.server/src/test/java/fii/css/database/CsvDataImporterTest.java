// CsvDataImporterTest.java
package fii.css.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvDataImporterTest {

    @Test
    void testParseCsvLine_simpleFields() {
        String line = "John,Doe,30,Engineer";
        String[] expected = {"John", "Doe", "30", "Engineer"};

        String[] result = CsvDataImporter.parseCsvLine(line);

        assertArrayEquals(expected, result, "Should correctly parse simple comma-separated fields");
    }

    @Test
    void testParseCsvLine_quotedFields() {
        String line = "\"John, Jr.\",Doe,30,\"Senior Engineer\"";
        String[] expected = {"John, Jr.", "Doe", "30", "Senior Engineer"};

        String[] result = CsvDataImporter.parseCsvLine(line);

        assertArrayEquals(expected, result, "Should correctly handle quoted fields containing commas");
    }

    @Test
    void testParseCsvLine_emptyFields() {
        String line = "John,,30,Engineer";
        String[] expected = {"John", "", "30", "Engineer"};

        String[] result = CsvDataImporter.parseCsvLine(line);

        assertArrayEquals(expected, result, "Should correctly handle empty fields");
    }

    @Test
    void testParseCsvLine_trailingComma() {
        String line = "John,Doe,30,";
        String[] expected = {"John", "Doe", "30", ""};

        String[] result = CsvDataImporter.parseCsvLine(line);

        assertArrayEquals(expected, result, "Should correctly handle a trailing comma as an empty field");
    }

    @Test
    void testParseCsvLine_quotedEmptyField() {
        String line = "John,\"\",30,Engineer";
        String[] expected = {"John", "", "30", "Engineer"};

        String[] result = CsvDataImporter.parseCsvLine(line);

        assertArrayEquals(expected, result, "Should correctly handle quoted empty fields");
    }

    @Test
    void testParseCsvLine_mismatchedQuotes() {
        String line = "\"John,Doe,30,Engineer";

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> CsvDataImporter.parseCsvLine(line),
                "Should throw an exception for mismatched quotes"
        );
        assertEquals("Mismatched quotes in CSV line", exception.getMessage());
    }

    @Test
    void testParseCsvLine_onlyQuotedField() {
        String line = "\"John\"";
        String[] expected = {"John"};

        String[] result = CsvDataImporter.parseCsvLine(line);

        assertArrayEquals(expected, result, "Should handle single quoted field correctly");
    }

    @Test
    void testParseCsvLine_onlyEmptyField() {
        String line = "";
        String[] expected = {""};

        String[] result = CsvDataImporter.parseCsvLine(line);

        assertArrayEquals(expected, result, "Should handle single empty field correctly");
    }

    @Test
    void testParseCsvLine_escapedQuotes() {
        String line = "\"John \"\"Johnny\"\"\",Doe";
        String[] expected = {"John \"Johnny\"", "Doe"};

        String[] result = CsvDataImporter.parseCsvLine(line);

        assertArrayEquals(expected, result, "Should correctly handle escaped quotes within quoted fields");
    }
}