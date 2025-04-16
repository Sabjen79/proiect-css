package fii.css.database.persistence.entities;

public class Room {
    private int roomId;
    private String name;
    private int capacity;
    private int roomTypeId;

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
