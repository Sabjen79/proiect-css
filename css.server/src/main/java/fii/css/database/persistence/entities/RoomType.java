package fii.css.database.persistence.entities;

import fii.css.database.DatabaseException;

public enum RoomType {
    Course(0),
    Laboratory(1);

    public final int value;

    RoomType(int value) {
        this.value = value;
    }

    public static RoomType fromValue(int value) {
        try {
            return values()[value];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DatabaseException("Invalid room type value: " + value);
        }
    }
}
