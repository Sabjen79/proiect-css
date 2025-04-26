package fii.css.database.persistence.managers;

import fii.css.database.Database;
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
        return repository.getById(id);
    }

    @Override
    public List<Discipline> getAll() {
        return repository.getAll();
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

    public void remove(String id) {
        Discipline discipline = repository.getById(id);
        if (discipline == null) {
            throw new RuntimeException("Discipline with ID " + id + " does not exist.");
        }

        // delete teacher discipline associations
        try {
            var connection = fii.css.database.Database.getInstance().getConnection();
            var stmt = connection.prepareStatement("DELETE FROM TeacherDiscipline WHERE discipline_id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete teacher-discipline associations for discipline with ID " + id, e);
        }

        // delete the discipline
        repository.delete(discipline);
    }
}
