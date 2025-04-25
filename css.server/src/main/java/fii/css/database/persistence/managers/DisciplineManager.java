package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Discipline;
import fii.css.database.persistence.repositories.AbstractRepository;
import fii.css.database.persistence.repositories.DisciplineRepository;

import java.util.List;

public class DisciplineManager extends AbstractEntityManager<Discipline>{
    public DisciplineManager() {
        super(new DisciplineRepository());
    }

    @Override
    public Discipline get(String id) {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Discipline> getAll() {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    public Discipline addDiscipline(String name, String description) {
        var entity = repository.newEntity();

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(discipline -> discipline.getName().equals(name));

        if (isDuplicate) throw new RuntimeException("Discipline with name " + name + " already exists.");

        entity.setName(name);
        entity.setDescription(description);

        repository.persist(entity);

        return entity;
    }

    public Discipline updateDiscipline(String id, String name, String description) {
        var entity = repository.getById(id);

        if (entity == null) throw new RuntimeException("Discipline with name " + name + " does not exist.");

        entity.setName(name);
        entity.setDescription(description);

        repository.merge(entity);

        return entity;
    }

    @Override
    public void remove(String id) {
        // TODO: Implement this (remember to delete entities from TeacherDiscipline )
        throw new UnsupportedOperationException();
    }
}
