package fii.css.database.persistence.entities;

import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;

public class Room {
    @Id
    @Column("room_id")
    private int roomId;

    @Column("name")
    private String name;

    @Column("capacity")
    private int capacity;

    @Column("room_type_id")
    private int roomTypeId;

    public Room() {
    }

    public Room(int roomId, String name, int capacity, int roomTypeId) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.roomTypeId = roomTypeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
}
