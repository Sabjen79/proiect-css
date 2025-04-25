package fii.css.database.persistence.managers;

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
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    @Override
    public List<FacultyGroup> getAll() {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    public FacultyGroup addFacultyGroup(String name, int year, StudyYear studyYear) {
        // TODO: Reimplement this :(
        throw new UnsupportedOperationException();
//        List<String> allowedPrefixes = List.of("A", "B", "E");
//
//        if (!isValidGroupName(name, allowedPrefixes)) {
//            throw new RuntimeException("Invalid group name: " + name + ". Must start with " + allowedPrefixes + " followed by a number (e.g., A1, B3).");
//        }
//
//        // Validare: sa nu existe deja o grupa cu acelasi nume si an de studiu
//        boolean duplicate = repository.getAll().stream()
//                .anyMatch(group -> group.getFacultyGroupName().equalsIgnoreCase(name)
//                        && group.getStudyYearId() == studyYearId);
//
//        if (duplicate) {
//            throw new RuntimeException("Faculty group '" + name + "' already exists for study year ID " + studyYearId);
//        }
//
//        FacultyGroup entity = repository.newEntity();
//        entity.setFacultyGroupName(name);
//        entity.setStudyYearId(studyYearId);
//
//        repository.persist(entity);
//        return entity;
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
//        FacultyGroup entity = repository.getById(id);
//        if (entity == null) {
//            throw new RuntimeException("Faculty group with ID " + id + " not found.");
//        }
//
//        boolean duplicate = repository.getAll().stream()
//                .anyMatch(group -> group.getFacultyGroupId() != id &&
//                        group.getFacultyGroupName().equalsIgnoreCase(newName) &&
//                        group.getStudyYearId() == newStudyYearId);
//
//        if (duplicate) {
//            throw new RuntimeException("Another faculty group with name '" + newName + "' already exists for study year ID " + newStudyYearId);
//        }
//
//        entity.setFacultyGroupName(newName);
//        entity.setStudyYearId(newStudyYearId);
//        repository.merge(entity);
//        return entity;
    }

    @Override
    public void remove(String id) {
        // TODO: Implement this ( remember to delete entities from Schedule )
        throw new UnsupportedOperationException();
    }
}
