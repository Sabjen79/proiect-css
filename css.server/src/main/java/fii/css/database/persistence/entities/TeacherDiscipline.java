package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

import java.util.UUID;

@Table("TeacherDiscipline")
public class TeacherDiscipline extends DatabaseEntity {
    @Id
    @Column("id")
    private String id;

    @Column("teacher_id")
    private String teacherId;

    @Column("discipline_id")
    private String disciplineId;

    public TeacherDiscipline() {}

    public String getId() {
        return id;
    }

    public void setId() {
        id = UUID.randomUUID().toString();
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String id) {
        this.teacherId = id;
    }

    public String getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(String id) {
        this.disciplineId = id;
    }

    @Override
    public DatabaseEntity clone() {
        var teacherDiscipline = new TeacherDiscipline();

        teacherDiscipline.id = id;
        teacherDiscipline.teacherId = this.teacherId;
        teacherDiscipline.disciplineId = this.disciplineId;

        return teacherDiscipline;
    }
}
