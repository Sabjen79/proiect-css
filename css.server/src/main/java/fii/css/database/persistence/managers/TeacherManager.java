package fii.css.database.persistence.managers;

import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Teacher;
import fii.css.database.persistence.entities.TeacherDiscipline;
import fii.css.database.persistence.repositories.TeacherDisciplineRepository;
import fii.css.database.persistence.repositories.TeacherRepository;

import java.util.List;

public class TeacherManager extends AbstractEntityManager<Teacher> {
    private final TeacherDisciplineRepository tdRepository;

    public TeacherManager() {
        super(new TeacherRepository());
        tdRepository = new TeacherDisciplineRepository();
    }

    @Override
    public Teacher get(String id) {
        var entity = repository.getById(id);

        entity.setDisciplineIds(
                tdRepository.getAll()
                        .stream()
                        .filter(td -> td.getTeacherId().equals(id))
                        .map(TeacherDiscipline::getId)
                        .toList()
        );

        return entity;
    }

    @Override
    public List<Teacher> getAll() {
        return repository.getAll().stream().map(t -> get(t.getId())).toList();
    }

    public void addTeacher(String name, String title, List<String> disciplineIds) {
        var entity = repository.newEntity();

        entity.setName(name);
        entity.setTitle(title);
        entity.setDisciplineIds(disciplineIds);

        validate(entity);

        for(var dId : disciplineIds) {
            var e = tdRepository.newEntity();

            e.setTeacherId(entity.getId());
            e.setDisciplineId(dId);

            tdRepository.persist(e);
        }

        repository.persist(entity);
    }

    public void updateTeacher(String id, String name, String title, List<String> disciplineIds) {
        var entity = repository.getById(id);

        entity.setName(name);
        entity.setTitle(title);
        entity.setDisciplineIds(disciplineIds);

        validate(entity);

        var teacherDisciplines = tdRepository
                .getAll()
                .stream()
                .filter(td -> td.getTeacherId().equals(id))
                .toList();

        // Remove TeacherDiscipline no longer used
        teacherDisciplines.stream()
                .filter(td -> !disciplineIds.contains(td.getDisciplineId()))
                .forEach(tdRepository::delete);

        // Add new TeacherDisciplines
        disciplineIds
                .stream()
                .filter(d -> teacherDisciplines
                        .stream()
                        .noneMatch(td -> td.getDisciplineId().equals(d)))
                .forEach(d -> {
                    var td = tdRepository.newEntity();

                    td.setTeacherId(entity.getId());
                    td.setDisciplineId(d);

                    tdRepository.persist(td);
                });

        repository.merge(entity);
    }

    @Override
    public void remove(String id) {
        Teacher teacher = repository.getById(id);
        if (teacher == null) {
            throw new DatabaseException("Teacher with ID " + id + " does not exist.");
        }

        tdRepository.getAll().forEach(td -> {
            if(td.getTeacherId().equals(id)) {
                tdRepository.delete(td);
            }
        });

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

        for(var d : teacher.getDisciplines()) {
            if(d == null) {
                throw new DatabaseException("One of the disciplines does not exist.");
            }
        }
    }
}
