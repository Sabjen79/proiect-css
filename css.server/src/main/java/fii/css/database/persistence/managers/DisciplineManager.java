package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Discipline;
import fii.css.database.persistence.repositories.AbstractRepository;

public class DisciplineManager extends AbstractEntityManager<Discipline>{
    public DisciplineManager(AbstractRepository<Discipline> repo) {
        super(repo);
    }

    public Discipline addDiscipline(String name, String description) {
        var entity = repository.newEntity();

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(discipline -> discipline.getName().equals(name));

        if (isDuplicate) throw new RuntimeException("Discipline with name " + name + "already exists.");

        entity.setName(name);
        entity.setDescription(description);

        repository.persist(entity);

        return entity;
    }

    public Discipline updateDiscipline(int id, String name, String description) {
        var entity = repository.getById(id);

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(discipline -> discipline.getName().equals(name));

        if (isDuplicate) throw new RuntimeException("Discipline with name " + name + "already exists.");

        entity.setName(name);
        entity.setDescription(description);

        repository.merge(entity);

        return entity;
    }

    public void removeDiscipline(int id) {
        repository.delete(repository.getById(id));
    }
}
