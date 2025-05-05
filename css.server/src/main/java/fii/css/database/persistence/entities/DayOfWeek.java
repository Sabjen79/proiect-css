package fii.css.database.persistence.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import fii.css.database.DatabaseException;

public enum DayOfWeek {
    Monday(0),
    Tuesday(1),
    Wednesday(2),
    Thursday(3),
    Friday(4);

    public final int value;

    DayOfWeek(int value) {
        this.value = value;
    }

    public static DayOfWeek fromValue(int value) {
        try {
            return values()[value];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DatabaseException("Invalid day of week value: " + value);
        }
    }

    @JsonValue
    public int toValue() {
        return value;
    }
}
