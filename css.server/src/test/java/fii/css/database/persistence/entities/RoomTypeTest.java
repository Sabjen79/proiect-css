package fii.css.database.persistence.entities;

import fii.css.database.DatabaseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTypeTest {

    @Test
    void testFromValueValid() {
        assertEquals(RoomType.Course, RoomType.fromValue(0));
        assertEquals(RoomType.Laboratory, RoomType.fromValue(1));
        assertEquals(RoomType.Seminary, RoomType.fromValue(2));
    }

    @Test
    void testFromValueInvalidThrowsException() {
        Exception ex = assertThrows(DatabaseException.class, () -> RoomType.fromValue(99));
        assertEquals("Invalid room type value: 99", ex.getMessage());
    }

    @Test
    void testToValue() {
        assertEquals(0, RoomType.Course.toValue());
        assertEquals(1, RoomType.Laboratory.toValue());
        assertEquals(2, RoomType.Seminary.toValue());
    }
}
