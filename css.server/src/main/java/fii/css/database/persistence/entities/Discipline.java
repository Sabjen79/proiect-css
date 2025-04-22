package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("discipline")
public class Discipline extends DatabaseEntity {
    @Id
    @Column("discipline_id")
    private int disciplineId;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    public Discipline() {}

    public int getDisciplineId() {
        return disciplineId;
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
