package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.Discipline;
import fii.css.database.persistence.repositories.AbstractRepository;
import fii.css.database.persistence.repositories.DisciplineRepository;
import fii.css.database.persistence.repositories.TeacherDisciplineRepository;

import java.util.List;

public class DisciplineManager extends AbstractEntityManager<Discipline> {
    private TeacherDisciplineRepository tdRepository;
    public DisciplineManager() {
        super(new DisciplineRepository());
    }

    public DisciplineManager(AbstractRepository<Discipline> repository) {
        this.repository = repository;
    }

    @Override
    public Discipline get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<Discipline> getAll() {
        return repository.getAll();
    }

    public void addDiscipline(String name, Degree degree, int year) {
        var entity = repository.newEntity();

        entity.setName(name.trim());
        entity.setDegree(degree);
        entity.setYear(year);

        validate(entity);

        repository.persist(entity);
    }

    public void updateDiscipline(String id, String name, Degree degree, int year) {
        var entity = repository.getById(id);

        if (entity == null) throw new DatabaseException("Discipline with name " + name + " does not exist.");

        entity.setName(name.trim());
        entity.setDegree(degree);
        entity.setYear(year);

        validate(entity);

        repository.merge(entity);
    }

    public void remove(String id) {
        Discipline discipline = repository.getById(id);
        if (discipline == null) {
            throw new RuntimeException("Discipline with ID " + id + " does not exist.");
        }

        var tdRepo = Database.getInstance().getTeacherDisciplineManager();
        for(var td : tdRepo.getAll()) {
            if(td.getDisciplineId().equals(id)) {
                throw new DatabaseException("Discipline is still associated with a teacher.");
            }
        }

        var sManager = Database.getInstance().getScheduleManager();
        for(var s : sManager.getAll()) {
            if(s.getDiscipline().getId().equals(id)) {
                throw new DatabaseException("Discipline is still referenced in schedule.");
            }
        }

        repository.delete(discipline);
    }

    private void validate(Discipline discipline) {
        assert discipline != null;
        assert discipline.getId() != null;
        assert discipline.getName() != null && !discipline.getName().isBlank();

        if(discipline.getYear() < 1) {
            throw new DatabaseException("Year must be greater than 0");
        }

        if(discipline.getDegree() == Degree.Bachelor && discipline.getYear() > 3) {
            throw new DatabaseException("Year must be less or equal than 3");
        }

        if(discipline.getDegree() == Degree.Master && discipline.getYear() > 2) {
            throw new DatabaseException("Year must be less or equal than 2");
        }

        for(var d : getAll()) {
            if (!d.getId().equalsIgnoreCase(discipline.getId())
                && d.getName().equalsIgnoreCase(discipline.getName())) {
                throw new DatabaseException("Discipline with name '" + discipline.getName() + "' already exists.");
            }
        }

        for(var s : Database.getInstance().getScheduleManager().getAll()) {
            if(s.getDiscipline().getId().equals(discipline.getId())) {
                if(s.getDiscipline().getYear() != discipline.getYear()) {
                    throw new DatabaseException("Year cannot be changed while the discipline is still referenced in schedule.");
                }
                if(s.getDiscipline().getDegree() != discipline.getDegree()) {
                    throw new DatabaseException("Degree be changed while the discipline is still referenced in schedule.");
                }
            }
        }
    }
}
