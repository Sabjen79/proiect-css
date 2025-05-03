package fii.css.database.persistence.repositories;

import fii.css.database.persistence.entities.Room;

public class RoomRepository extends AbstractRepository<Room> {
    public RoomRepository() {
        super(Room.class);
    }
}
