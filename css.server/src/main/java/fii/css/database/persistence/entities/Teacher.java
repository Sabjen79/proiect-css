package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

import java.util.ArrayList;
import java.util.List;

@Table("Teacher")
public class Teacher extends DatabaseEntity {
    @Id
    @Column("teacher_id")
    private String teacherId;

    @Column("name")
    private String name;

    @Column("title")
    private String title;

    private List<String> disciplineIds = new ArrayList<>();

    public Teacher() {}

    public String getId() {
        return teacherId;
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

    public List<Discipline> getDisciplines() {
        return disciplineIds
                .stream()
                .map(id -> Database.getInstance().disciplineManager.get(id))
                .toList();
    }

    public void setDisciplineIds(List<String> ids) {
        disciplineIds = ids;
    }

    @Override
    public DatabaseEntity clone() {
        var teacher = new Teacher();

        teacher.teacherId = this.teacherId;
        teacher.name = this.name;
        teacher.title = this.title;

        return teacher;
    }
}
