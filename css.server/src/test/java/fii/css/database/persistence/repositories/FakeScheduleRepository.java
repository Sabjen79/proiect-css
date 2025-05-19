package fii.css.database.persistence.repositories;

import fii.css.database.persistence.entities.Schedule;

import java.util.ArrayList;
import java.util.List;

public class FakeScheduleRepository extends AbstractRepository<Schedule> {
    private final List<Schedule> storage = new ArrayList<>();

    public FakeScheduleRepository() {
        super();
    }

    @Override
    public Schedule getById(String id) {
        return storage.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Schedule> getAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void delete(Schedule entity) {
        storage.removeIf(s -> s.getId().equals(entity.getId()));
    }

    @Override
    public Schedule newEntity() {
        Schedule schedule = new Schedule();
        schedule.setId();
        return schedule;
    }

    public void persist(Schedule entity) {
        // Doar dacă nu există deja
        if (getById(entity.getId()) == null) {
            storage.add(entity);
        }
    }

    public void merge(Schedule entity) {
        Schedule existing = getById(entity.getId());
        if (existing != null) {
            storage.remove(existing);
        }
        storage.add(entity);
    }
}
