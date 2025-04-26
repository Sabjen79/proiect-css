package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Discipline;
import fii.css.database.persistence.entities.Teacher;
import fii.css.database.persistence.entities.TeacherDiscipline;
import fii.css.database.persistence.repositories.TeacherDisciplineRepository;

import java.util.List;
import java.util.Objects;

public class TeacherDisciplineManager extends AbstractEntityManager<TeacherDiscipline> {
    public TeacherDisciplineManager() {
        super(new TeacherDisciplineRepository());
    }

    @Override
    public TeacherDiscipline get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<TeacherDiscipline> getAll() {
        return repository.getAll();
    }

    public TeacherDiscipline createTeacherDiscipline(Teacher teacher, Discipline discipline) {
        if (teacher == null || discipline == null) {
            throw new IllegalArgumentException("Teacher and Discipline must not be null.");
        }

        var isDuplicate = repository.getAll().stream()
                .anyMatch(td ->
                        Objects.equals(td.getTeacher().getId(), teacher.getId()) &&
                                Objects.equals(td.getDiscipline().getId(), discipline.getId()));

        if (isDuplicate) {
            throw new RuntimeException("This teacher is already assigned to this discipline.");
        }

        TeacherDiscipline entity = repository.newEntity();
        entity.setTeacher(teacher);
        entity.setDiscipline(discipline);

        repository.persist(entity);

        return entity;
    }

    public TeacherDiscipline updateTeacherDiscipline(String id, Teacher teacher, Discipline discipline) {
        TeacherDiscipline entity = repository.getById(id);
        if (entity == null) {
            throw new RuntimeException("TeacherDiscipline association with ID " + id + " does not exist.");
        }

        entity.setTeacher(teacher);
        entity.setDiscipline(discipline);

        repository.merge(entity);

        return entity;
    }


    @Override
    public void remove(String id) {
        TeacherDiscipline entity = repository.getById(id);
        if (entity == null) {
            throw new RuntimeException("TeacherDiscipline with ID " + id + " does not exist.");
        }

        //TODO delete from schedule

        repository.delete(entity);
    }
}
