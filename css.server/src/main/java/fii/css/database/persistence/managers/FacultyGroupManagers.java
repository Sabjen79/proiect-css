package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.FacultyGroup;
import fii.css.database.persistence.repositories.AbstractRepository;

import java.util.List;

public class FacultyGroupManagers extends AbstractEntityManager<FacultyGroup> {
    public FacultyGroupManagers(AbstractRepository<FacultyGroup> repo) {
        super(repo);
    }

    public FacultyGroup addFacultyGroup(String name, int studyYearId) {

        List<String> allowedPrefixes = List.of("A", "B", "E");

        if (!isValidGroupName(name, allowedPrefixes)) {
            throw new RuntimeException("Invalid group name: " + name + ". Must start with " + allowedPrefixes + " followed by a number (e.g., A1, B3).");
        }

        // Validare: sa nu existe deja o grupa cu acelasi nume si an de studiu
        boolean duplicate = repository.getAll().stream()
                .anyMatch(group -> group.getFacultyGroupName().equalsIgnoreCase(name)
                        && group.getStudyYearId() == studyYearId);

        if (duplicate) {
            throw new RuntimeException("Faculty group '" + name + "' already exists for study year ID " + studyYearId);
        }

        FacultyGroup entity = repository.newEntity();
        entity.setFacultyGroupName(name);
        entity.setStudyYearId(studyYearId);

        repository.persist(entity);
        return entity;
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

    public FacultyGroup updateFacultyGroup(int id, String newName, int newStudyYearId) {
        FacultyGroup entity = repository.getById(id);
        if (entity == null) {
            throw new RuntimeException("Faculty group with ID " + id + " not found.");
        }

        boolean duplicate = repository.getAll().stream()
                .anyMatch(group -> group.getFacultyGroupId() != id &&
                        group.getFacultyGroupName().equalsIgnoreCase(newName) &&
                        group.getStudyYearId() == newStudyYearId);

        if (duplicate) {
            throw new RuntimeException("Another faculty group with name '" + newName + "' already exists for study year ID " + newStudyYearId);
        }

        entity.setFacultyGroupName(newName);
        entity.setStudyYearId(newStudyYearId);
        repository.merge(entity);
        return entity;
    }

    public void deleteFacultyGroup(int id) {
        FacultyGroup group = repository.getById(id);
        if (group != null) {
            repository.delete(group);
        }
    }
}
