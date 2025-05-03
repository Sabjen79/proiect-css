package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.StudyYear;
import fii.css.database.persistence.repositories.StudyYearRepository;

import java.util.List;

public class StudyYearManager extends AbstractEntityManager<StudyYear> {
    public StudyYearManager() {
        super(new StudyYearRepository());
    }

    @Override
    public StudyYear get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<StudyYear> getAll() {
        return repository.getAll();
    }

    public void addStudyYear(Degree degree, String specialty, int maxYears) {
        StudyYear entity = repository.newEntity();
        entity.setDegree(degree);
        entity.setSpecialty(specialty.trim());
        entity.setMaxYears(maxYears);

        validate(entity);

        repository.persist(entity);
    }

    public void updateStudyYear(String id, Degree degree, String specialty, int maxYears) {
        StudyYear entity = repository.getById(id);

        if (entity == null) {
            throw new DatabaseException("Study year with ID '" + id + "' does not exist.");
        }

        entity.setDegree(degree);
        entity.setSpecialty(specialty.trim());
        entity.setMaxYears(maxYears);

        validate(entity);

        repository.merge(entity);
    }

    @Override
    public void remove(String id) {
        StudyYear studyYear = repository.getById(id);
        if (studyYear == null) {
            throw new DatabaseException("Study year with ID '" + id + "' does not exist.");
        }

        var fgManager = Database.getInstance().facultyGroupManager;
        fgManager.getAll().forEach(fg -> {
            if(fg.getStudyYear().getId().equals(id)) {
                fgManager.remove(fg.getId());
            }
        });

        repository.delete(studyYear);
    }

    private void validate(StudyYear sy) {
        // Being an enum, degree is always correct

        if(sy.getSpecialty() == null || sy.getSpecialty().trim().isEmpty()) {
            throw new DatabaseException("Specialty cannot be empty");
        }

        if(sy.getMaxYears() < 1) {
            throw new DatabaseException("Max years must be greater than 0");
        }

        for(StudyYear studyYear : getAll()) {
            if(!studyYear.getId().equals(sy.getId())
                    && studyYear.getDegree().equals(sy.getDegree())
                    && studyYear.getSpecialty().trim().equalsIgnoreCase(sy.getSpecialty())) {
                throw new DatabaseException("Specialty '" + sy.getSpecialty() + "' already exists for this degree.");
            }
        }
    }
}
