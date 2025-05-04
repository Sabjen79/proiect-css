package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
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
        return repository.getById(id);
    }

    @Override
    public List<Room> getAll() {
        return repository.getAll();
    }

    public void addRoom(String name, int capacity, RoomType roomType) {
        Room room = repository.newEntity();

        room.setName(name);
        room.setCapacity(capacity);
        room.setRoomType(roomType);

        validate(room);

        repository.persist(room);
    }

    public void updateRoom(String roomId, String name, int capacity, RoomType roomType) {
        Room room = repository.getById(roomId);

        if (room == null) {
            throw new RuntimeException("Room not found with ID: " + roomId);
        }

        room.setName(name);
        room.setCapacity(capacity);
        room.setRoomType(roomType);

        validate(room);

        repository.merge(room);
    }

    @Override
    public void remove(String id) {
        Room room = repository.getById(id);
        if (room == null) {
            throw new DatabaseException("Room with ID " + id + " does not exist.");
        }

        var sManager = Database.getInstance().scheduleManager;
        sManager.getAll().forEach(s -> {
            if(s.getRoom().getId().equals(id)) {
                throw new DatabaseException("Room is still referenced in schedule.");
            }
        });

        repository.delete(room);
    }

    private void validate(Room room) {
        if (room.getName() == null || room.getName().isBlank()) {
            throw new DatabaseException("Room name cannot be empty.");
        }

        if (room.getCapacity() <= 0) {
            throw new DatabaseException("Room capacity must be positive.");
        }

        for(var r : getAll()) {
            if (!r.getId().equalsIgnoreCase(room.getId())
                && r.getName().equalsIgnoreCase(room.getName())) {
                throw new DatabaseException("Room with name '" + room.getName() + "' already exists.");
            }
        }

        for(var s : Database.getInstance().scheduleManager.getAll()) {
            if(s.getRoom().getId().equals(room.getId())) {
                if(s.getRoom().getRoomType() != room.getRoomType()) {
                    throw new DatabaseException("Room type cannot be changed while it is still referenced in schedule.");
                }
            }
        }
    }
}
