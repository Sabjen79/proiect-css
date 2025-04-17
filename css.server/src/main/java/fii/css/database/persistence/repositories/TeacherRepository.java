package fii.css.database.persistence.repositories;

import fii.css.database.persistence.entities.Teacher;

public class TeacherRepository extends AbstractRepository<Teacher> {
    public TeacherRepository() {
        super(Teacher.class);
    }
}
