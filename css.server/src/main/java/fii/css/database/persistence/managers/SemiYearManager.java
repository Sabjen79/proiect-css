package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.SemiYear;
import fii.css.database.persistence.repositories.AbstractRepository;
import fii.css.database.persistence.repositories.SemiYearRepository;

import java.util.List;

public class SemiYearManager extends AbstractEntityManager<SemiYear> {
    public SemiYearManager(AbstractRepository<SemiYear> repository) {
        super(repository);
    }

    @Override
    public SemiYear get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<SemiYear> getAll() {
        return repository.getAll();
    }

    public void addStudyYear(String name, Degree degree, int year) {
        SemiYear entity = repository.newEntity();

        entity.setName(name.trim());
        entity.setDegree(degree);
        entity.setYear(year);

        validate(entity);

        repository.persist(entity);
    }

    public void updateStudyYear(String id, String name, Degree degree, int year) {
        SemiYear entity = repository.getById(id);

        if (entity == null) {
            throw new DatabaseException("Semi-year with ID '" + id + "' does not exist.");
        }

        entity.setName(name.trim());
        entity.setDegree(degree);
        entity.setYear(year);

        validate(entity);

        repository.merge(entity);
    }

    @Override
    public void remove(String id) {
        SemiYear studyYear = repository.getById(id);
        if (studyYear == null) {
            throw new DatabaseException("Semi-year with ID '" + id + "' does not exist.");
        }

        var fgManager = Database.getInstance().getFacultyGroupManager();
        fgManager.getAll().forEach(fg -> {
            if(fg.getSemiYearId().equals(id)) {
                throw new DatabaseException("Semi-year is still associated with group " + fg.getName());
            }
        });

        var sManager = Database.getInstance().getScheduleManager();
        sManager.getAll().forEach(s -> {
            if(s.getGroup().getIdFromAnnotation().equals(id)) {
                throw new DatabaseException("Semi-year is still referenced in schedule");
            }
        });

        repository.delete(studyYear);
    }

    private void validate(SemiYear sy) {
        assert sy != null;
        assert sy.getId() != null && !sy.getId().isBlank();
        assert sy.getName() != null && !sy.getName().isBlank();

        if(sy.getYear() < 1) {
            throw new DatabaseException("Year must be greater than 0");
        }

        if(sy.getDegree() == Degree.Bachelor && sy.getYear() > 3) {
            throw new DatabaseException("Year must be less or equal than 3");
        }

        if(sy.getDegree() == Degree.Master && sy.getYear() > 2) {
            throw new DatabaseException("Year must be less or equal than 2");
        }

        for(SemiYear studyYear : getAll()) {
            if(!studyYear.getId().equals(sy.getId())
                    && studyYear.getDegree().equals(sy.getDegree())
                    && studyYear.getYear() == sy.getYear()
                    && studyYear.getName().trim().equalsIgnoreCase(sy.getName())) {
                throw new DatabaseException("SemiYear '" + sy.getName() + "' already exists for this degree and year.");
            }
        }

        for(var s : Database.getInstance().getScheduleManager().getAll()) {
            if(s.getSemiYear().getId().equals(sy.getId())) {
                if(s.getSemiYear().getYear() != sy.getYear()) {
                    throw new DatabaseException("Year cannot be changed while this semi-year is still referenced in schedule.");
                }
                if(s.getSemiYear().getDegree() != sy.getDegree()) {
                    throw new DatabaseException("Degree cannot be changed while this semi-year is still referenced in schedule.");
                }
            }
        }
    }
}
