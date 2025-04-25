package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Discipline;
import fii.css.database.persistence.entities.Teacher;
import fii.css.database.persistence.entities.TeacherDiscipline;
import fii.css.database.persistence.repositories.AbstractRepository;
import fii.css.database.persistence.repositories.TeacherDisciplineRepository;

import java.util.List;

public class TeacherDisciplineManager extends AbstractEntityManager<TeacherDiscipline> {
    public TeacherDisciplineManager() {
        super(new TeacherDisciplineRepository());
    }

    @Override
    public TeacherDiscipline get(String id) {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TeacherDiscipline> getAll() {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    public TeacherDiscipline createTeacherDiscipline(Teacher teacher, Discipline discipline) {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    public TeacherDiscipline updateTeacherDiscipline(String id, Teacher teacher, Discipline discipline) {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(String id) {
        // TODO: Implement this ( remember to delete entities from Schedule )
        throw new UnsupportedOperationException();
    }
}
