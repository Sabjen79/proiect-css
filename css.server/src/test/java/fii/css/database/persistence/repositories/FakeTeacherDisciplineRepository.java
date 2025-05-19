package fii.css.database.persistence.repositories;

import fii.css.database.persistence.RandomId;
import fii.css.database.persistence.entities.TeacherDiscipline;

import java.util.ArrayList;
import java.util.List;

public class FakeTeacherDisciplineRepository extends AbstractRepository<TeacherDiscipline> {
    private final List<TeacherDiscipline> storage = new ArrayList<>();

    public FakeTeacherDisciplineRepository() {
        super();
    }

    @Override
    public TeacherDiscipline getById(String id) {
        return storage.stream()
                .filter(td -> td.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<TeacherDiscipline> getAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void persist(TeacherDiscipline entity) {
        if (entity.getId() == null) {
            try {
                var idField = TeacherDiscipline.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(entity, RandomId.newId());
                idField.setAccessible(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        storage.add(entity);
    }

    @Override
    public void delete(TeacherDiscipline entity) {
        storage.remove(entity);
    }

    @Override
    public TeacherDiscipline newEntity() {
        TeacherDiscipline td = new TeacherDiscipline();
        td.setId(); // sau o metodă echivalentă
        return td;
    }

    @Override
    public void merge(TeacherDiscipline updatedEntity) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getId().equals(updatedEntity.getId())) {
                storage.set(i, updatedEntity);
                return;
            }
        }
        throw new RuntimeException("Entity not found");
    }
}
