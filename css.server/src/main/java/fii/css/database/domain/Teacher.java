package fii.css.database.domain;

public class Teacher {

    private int teacherId;
    private String name;
    private String title;

    public Teacher(int teacherId, String name, String title) {
        this.teacherId = teacherId;
        this.name = name;
        this.title = title;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
