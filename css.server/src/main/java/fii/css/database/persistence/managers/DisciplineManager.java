package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Discipline;
import fii.css.database.persistence.entities.StudyYear;
import fii.css.database.persistence.repositories.AbstractRepository;
import fii.css.database.persistence.repositories.DisciplineRepository;
import fii.css.database.persistence.repositories.TeacherDisciplineRepository;

import java.util.List;

public class DisciplineManager extends AbstractEntityManager<Discipline> {
    private TeacherDisciplineRepository tdRepository;
    public DisciplineManager() {
        super(new DisciplineRepository());
    }

    @Override
    public Discipline get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<Discipline> getAll() {
        return repository.getAll();
    }

    public void addDiscipline(String name, String description, int year, String studyYearId) {
        var entity = repository.newEntity();

        entity.setName(name.trim());
        entity.setDescription(description.trim());
        entity.setYear(year);
        entity.setStudyYearId(studyYearId.trim());

        validate(entity);

        repository.persist(entity);
    }

    public void updateDiscipline(String id, String name, String description, int year, String studyYearId) {
        var entity = repository.getById(id);

        if (entity == null) throw new DatabaseException("Discipline with name " + name + " does not exist.");

        entity.setName(name.trim());
        entity.setDescription(description.trim());
        entity.setYear(year);
        entity.setStudyYearId(studyYearId.trim());

        validate(entity);

        repository.merge(entity);
    }

    public void remove(String id) {
        Discipline discipline = repository.getById(id);
        if (discipline == null) {
            throw new RuntimeException("Discipline with ID " + id + " does not exist.");
        }

        var tdRepo = new TeacherDisciplineRepository();
        tdRepo.getAll().forEach(td -> {
            if(td.getDisciplineId().equals(id)) {
                tdRepo.delete(td);
            }
        });

        repository.delete(discipline);
    }

    private void validate(Discipline discipline) {
        if(discipline.getName() == null || discipline.getName().isBlank()) {
            throw new DatabaseException("Discipline name cannot be empty.");
        }

        if(discipline.getDescription() == null || discipline.getDescription().isBlank()) {
            throw new DatabaseException("Discipline description cannot be empty.");
        }

        var studyYear = discipline.getStudyYear();

        if(studyYear == null) {
            throw new DatabaseException("The specified study year does not exist.");
        }

        if(discipline.getYear() < 1 || discipline.getYear() > discipline.getStudyYear().getMaxYears()) {
            throw new DatabaseException("Study year must be between 1 and " + (studyYear.getMaxYears()));
        }

        for(var d : getAll()) {
            if (!d.getId().equalsIgnoreCase(discipline.getId())
                    && d.getName().equalsIgnoreCase(discipline.getName())) {
                throw new DatabaseException("Discipline with name '" + discipline.getName() + "' already exists.");
            }
        }
    }
}
