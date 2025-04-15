package fii.css.database.domain;

public class RoomType {
    private int roomTypeId;
    private String typeName;

    public RoomType(int roomTypeId, String typeName) {
        this.roomTypeId = roomTypeId;
        this.typeName = typeName;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
