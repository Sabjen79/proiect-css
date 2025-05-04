package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

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

    public String getId() {
        return roomId;
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
