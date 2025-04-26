package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Teacher;
import fii.css.database.persistence.repositories.TeacherRepository;

import java.util.List;
import java.util.Objects;

public class TeacherManager extends AbstractEntityManager<Teacher> {
    public TeacherManager() {
        super(new TeacherRepository());
    }

    @Override
    public Teacher get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<Teacher> getAll() {
        return repository.getAll();
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
                .anyMatch(teacher -> teacher.getName().equals(name) && !Objects.equals(teacher.getId(), id));

        if (isDuplicate) throw new RuntimeException("Teacher with name " + name + " already exists");

        entity.setName(name);
        entity.setTitle(title);

        repository.merge(entity);

        return entity;
    }

    @Override
    public void remove(String id) {
        Teacher teacher = repository.getById(id);
        if (teacher == null) {
            throw new RuntimeException("Teacher with ID " + id + " does not exist.");
        }
        // delete association from TeacherDiscipline
        try {
            var connection = fii.css.database.Database.getInstance().getConnection();
            var stmt = connection.prepareStatement("DELETE FROM TeacherDiscipline WHERE teacher_id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete teacher-discipline associations for teacher with ID " + id, e);
        }
        // delete the teacher
        repository.delete(teacher);
    }
}
