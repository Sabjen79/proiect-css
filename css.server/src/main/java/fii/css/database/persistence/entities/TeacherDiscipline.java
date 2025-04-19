package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("TeacherDiscipline")
public class TeacherDiscipline extends DatabaseEntity {
    @Id
    @Column("teacher_discipline_id")
    private int teacherDisciplineId;

    @Column("teacher_id")
    private int teacherId;

    @Column("discipline_id")
    private int disciplineId;

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getTeacherDisciplineId() {
        return teacherDisciplineId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public int getDisciplineId() {
        return disciplineId;
    }
}
