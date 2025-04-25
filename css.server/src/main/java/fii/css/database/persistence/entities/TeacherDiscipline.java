package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("TeacherDiscipline")
public class TeacherDiscipline extends DatabaseEntity {
    @Id
    @Column("teacher_discipline_id")
    private String teacherDisciplineId;

    @Column("teacher_id")
    private String teacherId;

    @Column("discipline_id")
    private String disciplineId;

    public TeacherDiscipline() {}

    public String getId() {
        return teacherDisciplineId;
    }

    public Teacher getTeacher() {
        return Database
            .getInstance()
            .teacherManager
            .get(teacherId);
    }

    public void setTeacher(Teacher teacher) {
        this.teacherId = teacher.getId();
    }

    public Discipline getDiscipline() {
        return Database
            .getInstance()
            .disciplineManager
            .get(disciplineId);
    }

    public void setDiscipline(Discipline discipline) {
        this.disciplineId = discipline.getId();
    }
}
