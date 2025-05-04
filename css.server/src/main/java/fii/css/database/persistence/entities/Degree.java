package fii.css.database.persistence.entities;

import fii.css.database.DatabaseException;

public enum Degree {
    Bachelor(0),
    Master(1);

    public final int value;

    Degree(int value) {
        this.value = value;
    }

    public static Degree fromValue(int value) {
        try {
            return values()[value];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DatabaseException("Invalid degree value: " + value);
        }
    }
}
