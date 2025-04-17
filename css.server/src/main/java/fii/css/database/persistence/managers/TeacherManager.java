package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Teacher;
import fii.css.database.persistence.repositories.TeacherRepository;

public class TeacherManager extends AbstractEntityManager<Teacher> {
    public TeacherManager() {
        super(new TeacherRepository());
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

    public Teacher updateTeacher(int id, String name, String title) {
        var entity = repository.getById(id);

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(teacher -> teacher.getName().equals(name) && teacher.getTeacherId() != id);

        if (isDuplicate) throw new RuntimeException("Teacher with name " + name + " already exists");

        entity.setName(name);
        entity.setTitle(title);

        repository.merge(entity);

        return entity;
    }

    public void removeTeacher(int id) {
        repository.delete(repository.getById(id));
    }
}
