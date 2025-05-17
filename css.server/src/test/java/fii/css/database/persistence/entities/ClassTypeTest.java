package fii.css.database.persistence.entities;

import fii.css.database.DatabaseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClassTypeTest {

    @Test
    void testFromValueValid() {
        assertEquals(ClassType.Course, ClassType.fromValue(0));
        assertEquals(ClassType.Laboratory, ClassType.fromValue(1));
        assertEquals(ClassType.Seminary, ClassType.fromValue(2));
    }

    @Test
    void testFromValueInvalidThrowsException() {
        Exception ex = assertThrows(DatabaseException.class, () -> ClassType.fromValue(99));
        assertEquals("Invalid class type value: 99", ex.getMessage());
    }

    @Test
    void testToValue() {
        assertEquals(0, ClassType.Course.toValue());
        assertEquals(1, ClassType.Laboratory.toValue());
        assertEquals(2, ClassType.Seminary.toValue());
    }
}
