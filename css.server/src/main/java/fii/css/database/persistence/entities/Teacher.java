package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table("Teacher")
public class Teacher extends DatabaseEntity {
    @Id
    @Column("id")
    private String id;

    @Column("name")
    private String name;

    @Column("title")
    private String title;

    public Teacher() {}

    public String getId() {
        return id;
    }

    public void setId() {
        id = UUID.randomUUID().toString();
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

    @Override
    public DatabaseEntity clone() {
        var teacher = new Teacher();

        teacher.id = this.id;
        teacher.name = this.name;
        teacher.title = this.title;

        return teacher;
    }
}
