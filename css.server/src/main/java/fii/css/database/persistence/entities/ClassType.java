package fii.css.database.persistence.entities;

public enum ClassType {
    Course(0),
    Laboratory(1),
    Seminary(2),
    Exam(3);

    public final int value;

    private ClassType(int value) {
        this.value = value;
    }

    public static ClassType fromValue(int value) {
        return values()[value];
    }
}
