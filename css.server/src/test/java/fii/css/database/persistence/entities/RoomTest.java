package fii.css.database.persistence.entities;

import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    @Test
    void testGettersAndSetters() {
        Room room = new Room();

        room.setName("Lab 1");
        room.setCapacity(30);
        room.setRoomType(RoomType.Laboratory);

        assertNull(room.getId(), "Room ID should be null by default");
        assertEquals("Lab 1", room.getName());
        assertEquals(30, room.getCapacity());
        assertEquals(RoomType.Laboratory, room.getRoomType());
    }

    @Test
    void testCloneCreatesEqualCopy() {
        Room original = new Room();
        original.setName("Seminar 1");
        original.setCapacity(20);
        original.setRoomType(RoomType.Seminary);

        // Set private ID field via reflection
        try {
            Field idField = Room.class.getDeclaredField("roomId");
            idField.setAccessible(true);
            idField.set(original, "R001");
        } catch (Exception e) {
            fail("Failed to set private roomId via reflection: " + e.getMessage());
        }

        Room clone = (Room) original.clone();

        assertNotSame(original, clone);
        assertEquals(original.getId(), clone.getId());
        assertEquals(original.getName(), clone.getName());
        assertEquals(original.getCapacity(), clone.getCapacity());
        assertEquals(original.getRoomType(), clone.getRoomType());
    }

    @Test
    void testAnnotationsPresence() throws Exception {
        assertTrue(Room.class.isAnnotationPresent(Table.class), "Class should be annotated with @Table");
        Table table = Room.class.getAnnotation(Table.class);
        assertEquals("Room", table.value());

        Field idField = Room.class.getDeclaredField("roomId");
        assertTrue(idField.isAnnotationPresent(Id.class));
        assertTrue(idField.isAnnotationPresent(Column.class));
        assertEquals("id", idField.getAnnotation(Column.class).value());

        Field nameField = Room.class.getDeclaredField("name");
        assertTrue(nameField.isAnnotationPresent(Column.class));
        assertEquals("name", nameField.getAnnotation(Column.class).value());

        Field capField = Room.class.getDeclaredField("capacity");
        assertTrue(capField.isAnnotationPresent(Column.class));
        assertEquals("capacity", capField.getAnnotation(Column.class).value());

        Field typeField = Room.class.getDeclaredField("roomType");
        assertTrue(typeField.isAnnotationPresent(Column.class));
        assertEquals("room_type", typeField.getAnnotation(Column.class).value());
    }

    @Test
    void testClonePreservesValues() {
        Room original = new Room();
        original.setName("Conference Room");
        original.setCapacity(100);
        original.setRoomType(RoomType.Course);

        // Set ID via reflection
        try {
            Field idField = Room.class.getDeclaredField("roomId");
            idField.setAccessible(true);
            idField.set(original, "CONF01");

            Room clone = (Room) original.clone();

            assertEquals("CONF01", clone.getId());
            assertEquals("Conference Room", clone.getName());
            assertEquals(100, clone.getCapacity());
            assertEquals(RoomType.Course, clone.getRoomType());

        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }
}
