package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Teacher;
import fii.css.database.persistence.entities.TeacherDiscipline;
import fii.css.database.persistence.repositories.AbstractRepository;
import fii.css.database.persistence.repositories.TeacherDisciplineRepository;
import fii.css.database.persistence.repositories.TeacherRepository;

import java.util.List;

public class TeacherManager extends AbstractEntityManager<Teacher> {
    public TeacherManager() {
        super(new TeacherRepository());
    }

    public TeacherManager(AbstractRepository<Teacher> repository) {
        this.repository = repository;
    }

    @Override
    public Teacher get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<Teacher> getAll() {
        return repository.getAll();
    }

    public void addTeacher(String name, String title) {
        var entity = repository.newEntity();

        entity.setName(name);
        entity.setTitle(title);

        validate(entity);

        repository.persist(entity);
    }

    public void updateTeacher(String id, String name, String title) {
        var entity = repository.getById(id);

        entity.setName(name);
        entity.setTitle(title);

        validate(entity);

        repository.merge(entity);
    }

    @Override
    public void remove(String id) {
        Teacher teacher = repository.getById(id);
        if (teacher == null) {
            throw new DatabaseException("Teacher with ID " + id + " does not exist.");
        }

        var tdRepo = Database.getInstance().teacherDisciplineManager;
        for(var td : tdRepo.getAll()) {
            if(td.getTeacherId().equals(id)) {
                throw new DatabaseException("Teacher is still associated with a discipline.");
            }
        }

        var sManager = Database.getInstance().scheduleManager;
        for(var s : sManager.getAll()) {
            if(s.getTeacher().getId().equals(id)) {
                throw new DatabaseException("Teacher is still referenced in schedule.");
            }
        }

        repository.delete(teacher);
    }

    private void validate(Teacher teacher) {
        if (teacher.getName() == null || teacher.getName().isBlank()) {
            throw new DatabaseException("Teacher name cannot be empty.");
        }

        if (teacher.getTitle() == null || teacher.getTitle().isBlank()) {
            throw new DatabaseException("Teacher title cannot be empty.");
        }

        for(var t : getAll()) {
            if (!t.getId().equalsIgnoreCase(teacher.getId())
                    && t.getName().equalsIgnoreCase(teacher.getName())) {
                throw new DatabaseException("Teacher with name '" + teacher.getName() + "' already exists.");
            }
        }
    }
}
