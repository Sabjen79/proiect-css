package fii.css.database.persistence.repositories;


import fii.css.database.persistence.RandomId;
import fii.css.database.persistence.entities.Teacher;
import fii.css.database.persistence.entities.TeacherDiscipline;

import java.util.ArrayList;
import java.util.List;

public class FakeTeacherRepository extends AbstractRepository<Teacher>{

    private final List<Teacher> storage = new ArrayList<>();

    public FakeTeacherRepository() {
        super();
    }

    @Override
    public Teacher getById(String id) {
        return storage.stream()
                .filter(td -> td.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Teacher> getAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void persist(Teacher entity) {
        if (entity.getId() == null) {
            try {
                var idField = Teacher.class.getDeclaredField("id");
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
    public void delete(Teacher entity) {
        storage.remove(entity);
    }

    @Override
    public Teacher newEntity() {
        Teacher td = new Teacher();
        td.setId(); // sau o metodă echivalentă
        return td;
    }

    @Override
    public void merge(Teacher updatedEntity) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getId().equals(updatedEntity.getId())) {
                storage.set(i, updatedEntity);
                return;
            }
        }
        throw new RuntimeException("Entity not found");
    }

}
