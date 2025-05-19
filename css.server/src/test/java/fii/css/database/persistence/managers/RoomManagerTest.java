package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Room;
import fii.css.database.persistence.entities.Schedule;
import fii.css.database.persistence.repositories.FakeDisciplineRepository;
import fii.css.database.persistence.repositories.FakeRoomRepository;
import fii.css.database.persistence.repositories.FakeScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import fii.css.database.persistence.entities.RoomType;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomManagerTest {
    RoomManager roomManager;
    ScheduleManager scheduleManager;

    @BeforeEach
    void setUp() {
        FakeRoomRepository fakeRepo = new FakeRoomRepository();
        roomManager = new RoomManager(fakeRepo);

        FakeScheduleRepository fakeScheduleRepo = new FakeScheduleRepository();
        scheduleManager = new ScheduleManager(fakeScheduleRepo);

        var db = fii.css.database.Database.getInstance();
        db.setRoomManager(roomManager);
        db.setScheduleManager(scheduleManager);

    }

    @Test
    void testAddRoomSuccess() {
        roomManager.addRoom("Room A", 20, RoomType.Laboratory);
        List<Room> all = roomManager.getAll();
        assertEquals(1, all.size());
        assertEquals("Room A", all.get(0).getName());
        assertEquals(20, all.get(0).getCapacity());
        assertEquals(RoomType.Laboratory, all.get(0).getRoomType());
    }

    @Test
    void testAddRoomInvalidCapacityThrowsException() {
        var ex = assertThrows(DatabaseException.class, () ->
                roomManager.addRoom("Room B", 0, RoomType.Course));
        assertTrue(ex.getMessage().contains("capacity must be positive"));
    }

    @Test
    void testAddRoomDuplicateNameThrowsException() {
        roomManager.addRoom("Room C", 25, RoomType.Laboratory);
        var ex = assertThrows(DatabaseException.class, () ->
                roomManager.addRoom("Room C", 30, RoomType.Course));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    void testUpdateRoomSuccess() {
        roomManager.addRoom("Room D", 15, RoomType.Laboratory);
        Room room = roomManager.getAll().get(0);
        roomManager.updateRoom(room.getId(), "Room D Updated", 40, RoomType.Laboratory);

        Room updated = roomManager.get(room.getId());
        assertEquals("Room D Updated", updated.getName());
        assertEquals(40, updated.getCapacity());
    }

    @Test
    void testUpdateRoomNotFoundThrowsException() {
        var ex = assertThrows(RuntimeException.class, () ->
                roomManager.updateRoom("nonexistent-id", "X", 10, RoomType.Laboratory));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void testRemoveRoomSuccess() {
        roomManager.addRoom("Room E", 10, RoomType.Course);
        Room room = roomManager.getAll().get(0);
        roomManager.remove(room.getId());

        assertTrue(roomManager.getAll().isEmpty());
    }


}
