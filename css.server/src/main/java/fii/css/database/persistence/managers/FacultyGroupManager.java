package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.persistence.entities.FacultyGroup;
import fii.css.database.persistence.entities.StudyYear;
import fii.css.database.persistence.repositories.FacultyGroupRepository;

import java.util.List;

public class FacultyGroupManager extends AbstractEntityManager<FacultyGroup> {
    public FacultyGroupManager() {
        super(new FacultyGroupRepository());
    }

    @Override
    public FacultyGroup get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<FacultyGroup> getAll() {
        return repository.getAll();
    }

    public FacultyGroup addFacultyGroup(String name, int year, StudyYear studyYear) {
        // TODO: Reimplement this :(
        throw new UnsupportedOperationException();

    }

    private boolean isValidGroupName(String name, List<String> allowedPrefixes) {
        if (name.length() < 2) return false;

        String prefix = name.substring(0, 1).toUpperCase();
        String numberPart = name.substring(1);

        if (!allowedPrefixes.contains(prefix)) return false;

        try {
            int number = Integer.parseInt(numberPart);
            return number >= 1; // sau >= 1 && <= 6 dacă vrei să limitezi
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public FacultyGroup updateFacultyGroup(String id, String name, int year, StudyYear studyYear) {
        // TODO: Reimplement this :(
        throw new UnsupportedOperationException();

    }

    @Override
    public void remove(String id) {
        FacultyGroup group = repository.getById(id);
        if (group == null) {
            throw new RuntimeException("Faculty group with ID " + id + " not found.");
        }

        // delete schedules for this faculty group
        var schedules = Database.getInstance().scheduleManager.getAll();
        schedules.stream()
                .filter(schedule -> schedule.getFacultyGroup().getId().equals(id))
                .forEach(schedule -> Database.getInstance().scheduleManager.remove(schedule.getId()));

        // delete the faculty group
        repository.delete(group);
    }
}
