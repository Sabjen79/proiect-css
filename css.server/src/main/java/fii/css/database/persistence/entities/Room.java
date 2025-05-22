package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

import java.util.UUID;

@Table("Room")
public class Room extends DatabaseEntity{
    @Id
    @Column("id")
    private String roomId;

    @Column("name")
    private String name;

    @Column("capacity")
    private int capacity;

    @Column("room_type")
    private int roomType;

    public Room() { }

    public Room(String roomId, String name, int capacity, int roomType) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.roomType = roomType;
    }

    public String getId() {
        return roomId;
    }

    public void setId() {
        roomId = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomType getRoomType() {
        assert roomType > -1 && roomType < RoomType.values().length;

        return RoomType.fromValue(roomType);
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType.value;
    }

    @Override
    public DatabaseEntity clone() {
        var room = new Room();

        room.roomId = this.roomId;
        room.name = this.name;
        room.capacity = this.capacity;
        room.roomType = this.roomType;

        return room;
    }
}
