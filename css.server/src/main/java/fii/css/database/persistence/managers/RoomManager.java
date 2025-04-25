package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Room;
import fii.css.database.persistence.entities.RoomType;
import fii.css.database.persistence.repositories.AbstractRepository;
import fii.css.database.persistence.repositories.RoomRepository;

import java.util.List;

public class RoomManager extends AbstractEntityManager<Room> {
    public RoomManager() {
        super(new RoomRepository());
    }

    @Override
    public Room get(String id) {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Room> getAll() {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    public Room addRoom(String name, int capacity, RoomType roomType) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Room name cannot be empty.");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Room capacity must be positive.");
        }

        boolean nameExists = repository.getAll().stream()
                .anyMatch(room -> room.getName().equalsIgnoreCase(name));

        if (nameExists) {
            throw new RuntimeException("Room with name '" + name + "' already exists.");
        }

        Room room = repository.newEntity();
        room.setName(name);
        room.setCapacity(capacity);
        room.setRoomType(roomType);

        repository.persist(room);
        return room;
    }

    public Room updateRoom(String roomId, String name, int capacity, RoomType roomType) {
        Room room = repository.getById(roomId);
        if (room == null) {
            throw new RuntimeException("Room not found with ID: " + roomId);
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Room name cannot be empty.");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Room capacity must be positive.");
        }

        boolean duplicate = repository.getAll().stream()
                .anyMatch(r -> r.getName().equalsIgnoreCase(name) && !r.getId().equals(roomId));

        if (duplicate) {
            throw new RuntimeException("Another room with name '" + name + "' already exists.");
        }

        room.setName(name);
        room.setCapacity(capacity);
        room.setRoomType(roomType);

        repository.merge(room);
        return room;
    }

    public void removeRoom(String roomId) {
        Room room = repository.getById(roomId);
        if (room != null) {
            repository.delete(room);
        } else {
            throw new RuntimeException("Room with ID " + roomId + " does not exist.");
        }
    }

    @Override
    public void remove(String id) {
        // TODO: Implement this ( remember to delete entities from Schedule )
        throw new UnsupportedOperationException();
    }
}
