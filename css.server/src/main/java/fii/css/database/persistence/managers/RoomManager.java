package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Room;
import fii.css.database.persistence.entities.RoomType;
import fii.css.database.persistence.repositories.AbstractRepository;

public class RoomManager extends AbstractEntityManager<Room> {
    private final AbstractRepository<RoomType> roomTypeRepository;

    public RoomManager(AbstractRepository<Room> repository, AbstractRepository<RoomType> roomTypeRepository) {
        super(repository);
        this.roomTypeRepository = roomTypeRepository;
    }

    public Room addRoom(String name, int capacity, int roomTypeId) {
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

        RoomType roomType = roomTypeRepository.getById(roomTypeId);
        if (roomType == null) {
            throw new RuntimeException("Invalid RoomType ID: " + roomTypeId);
        }

        room.setRoomTypeId(roomTypeId);

        repository.persist(room);
        return room;
    }

    public Room updateRoom(int roomId, String name, int capacity, int roomTypeId) {
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
                .anyMatch(r -> r.getName().equalsIgnoreCase(name) && r.getRoomId() != roomId);

        if (duplicate) {
            throw new RuntimeException("Another room with name '" + name + "' already exists.");
        }

        room.setName(name);
        room.setCapacity(capacity);
        room.setRoomTypeId(roomTypeId);

        repository.merge(room);
        return room;
    }

    public void removeRoom(int roomId) {
        Room room = repository.getById(roomId);
        if (room != null) {
            repository.delete(room);
        } else {
            throw new RuntimeException("Room with ID " + roomId + " does not exist.");
        }
    }
}
