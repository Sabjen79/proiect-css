package fii.css.database.persistence.entities;

import fii.css.database.DatabaseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DayOfWeekTest {

    @Test
    void testFromValueValid() {
        assertEquals(DayOfWeek.Monday, DayOfWeek.fromValue(0));
        assertEquals(DayOfWeek.Tuesday, DayOfWeek.fromValue(1));
        assertEquals(DayOfWeek.Wednesday, DayOfWeek.fromValue(2));
        assertEquals(DayOfWeek.Thursday, DayOfWeek.fromValue(3));
        assertEquals(DayOfWeek.Friday, DayOfWeek.fromValue(4));
    }

    @Test
    void testFromValueInvalidThrowsException() {
        Exception ex = assertThrows(DatabaseException.class, () -> DayOfWeek.fromValue(5));
        assertEquals("Invalid day of week value: 5", ex.getMessage());
    }

    @Test
    void testToValue() {
        assertEquals(0, DayOfWeek.Monday.toValue());
        assertEquals(1, DayOfWeek.Tuesday.toValue());
        assertEquals(2, DayOfWeek.Wednesday.toValue());
        assertEquals(3, DayOfWeek.Thursday.toValue());
        assertEquals(4, DayOfWeek.Friday.toValue());
    }
}

