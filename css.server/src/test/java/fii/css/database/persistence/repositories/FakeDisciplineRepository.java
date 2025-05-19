package fii.css.database.persistence.repositories;

import fii.css.database.persistence.RandomId;
import fii.css.database.persistence.entities.Discipline;

import java.util.ArrayList;
import java.util.List;

public class FakeDisciplineRepository extends AbstractRepository<Discipline> {
    private final List<Discipline> storage = new ArrayList<>();

    public FakeDisciplineRepository() {
        super();
    }

    @Override
    public Discipline getById(String id) {
        return storage.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Discipline> getAll() {
        return new ArrayList<>(storage);
    }

//    @Override
//    public void persist(Discipline entity) {
//        storage.add(entity);
//    }

    @Override
    public void delete(Discipline entity) {
        storage.remove(entity);
    }

    @Override
    public Discipline newEntity() {
        Discipline d = new Discipline();
        d.setId();
        return d;
    }

    @Override
    public void merge(Discipline updatedEntity) {
        // implementare simplă pentru test, actualizează lista entities direct
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getIdFromAnnotation().equals(updatedEntity.getIdFromAnnotation())) {
                storage.set(i, updatedEntity);
                return;
            }
        }
        throw new RuntimeException("Entity not found");
    }

    @Override
    public void persist(Discipline entity) {
        if (entity.getIdFromAnnotation() == null) {
            // setează un ID random dacă nu există deja
            // (folosește RandomId sau o simplă generare)
            try {
                var idField = Discipline.class.getDeclaredField("id"); // sau cum ai definit ID-ul
                idField.setAccessible(true);
                idField.set(entity, RandomId.newId());
                idField.setAccessible(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        storage.add(entity);
    }
}

