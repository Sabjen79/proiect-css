package fii.css.database.persistence.entities;

public enum Degree {
    Bachelor(0),
    Master(1),
    PhD(2);

    public final int value;

    Degree(int value) {
        this.value = value;
    }

    public static Degree fromValue(int value) {
        return values()[value];
    }
}
