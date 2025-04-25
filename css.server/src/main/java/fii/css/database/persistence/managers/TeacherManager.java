package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Teacher;
import fii.css.database.persistence.repositories.TeacherRepository;

import java.util.List;

public class TeacherManager extends AbstractEntityManager<Teacher> {
    public TeacherManager() {
        super(new TeacherRepository());
    }

    @Override
    public Teacher get(String id) {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Teacher> getAll() {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    public Teacher addTeacher(String name, String title) {
        var entity = repository.newEntity();

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(teacher -> teacher.getName().equals(name));

        if (isDuplicate) throw new RuntimeException("Teacher with name " + name + " already exists");

        entity.setName(name);
        entity.setTitle(title);

        repository.persist(entity);

        return entity;
    }

    public Teacher updateTeacher(String id, String name, String title) {
        var entity = repository.getById(id);

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(teacher -> teacher.getName().equals(name) && teacher.getId() != id);

        if (isDuplicate) throw new RuntimeException("Teacher with name " + name + " already exists");

        entity.setName(name);
        entity.setTitle(title);

        repository.merge(entity);

        return entity;
    }

    @Override
    public void remove(String id) {
        // TODO: Implement this (remember to delete entities from TeacherDiscipline )
        throw new UnsupportedOperationException();
    }
}
