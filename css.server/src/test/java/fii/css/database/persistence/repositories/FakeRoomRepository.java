package fii.css.database.persistence.repositories;


import fii.css.database.persistence.RandomId;
import fii.css.database.persistence.entities.Room;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FakeRoomRepository extends AbstractRepository<Room>{

    private final List<Room> storage = new ArrayList<>();

    public FakeRoomRepository() {
        super();
    }

    @Override
    public Room getById(String id) {
        return storage.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Room> getAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void persist(Room entity) {
        if (entity.getId() == null) {
            try {
                Field idField = Room.class.getDeclaredField("roomId");
                idField.setAccessible(true);
                idField.set(entity, RandomId.newId());
                idField.setAccessible(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        storage.add(entity);
    }

    @Override
    public void delete(Room entity) {
        storage.remove(entity);
    }

    @Override
    public Room newEntity() {
        Room room = new Room();
        room.setId();  // setează UUID
        return room;
    }

    @Override
    public void merge(Room updatedEntity) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getId().equals(updatedEntity.getId())) {
                storage.set(i, updatedEntity);
                return;
            }
        }
        throw new RuntimeException("Room not found");
    }
}
