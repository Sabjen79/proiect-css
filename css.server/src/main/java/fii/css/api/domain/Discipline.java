package fii.css.api.domain;

public class Discipline {
    private int disciplineId;
    private String name;
    private String description;

    public Discipline(int disciplineId, String name, String description) {
        this.disciplineId = disciplineId;
        this.name = name;
        this.description = description;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
