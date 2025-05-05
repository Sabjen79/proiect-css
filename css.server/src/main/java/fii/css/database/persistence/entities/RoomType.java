package fii.css.database.persistence.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import fii.css.database.DatabaseException;

public enum RoomType {
    Course(0),
    Laboratory(1),
    Seminary(2);

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

    @JsonValue
    public int toValue() {
        return value;
    }
}
