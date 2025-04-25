package fii.css.database.persistence.entities;

public enum RoomType {
    Course(0),
    Seminary(1),
    Laboratory(2),
    Office(3),
    Equipment(4);

    public final int value;

    RoomType(int value) {
        this.value = value;
    }

    public static RoomType fromValue(int value) {
        return values()[value];
    }
}
