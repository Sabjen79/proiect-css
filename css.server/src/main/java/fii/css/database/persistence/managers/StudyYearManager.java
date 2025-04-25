package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.StudyYear;
import fii.css.database.persistence.repositories.StudyYearRepository;

import java.util.List;

public class StudyYearManager extends AbstractEntityManager<StudyYear> {
    public StudyYearManager() {super(new StudyYearRepository());}

    @Override
    public StudyYear get(String id) {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    @Override
    public List<StudyYear> getAll() {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    public StudyYear addStudyYear(Degree degree, String specialty, int max_years) {
        // TODO: Reimplement this :(
        throw new UnsupportedOperationException();
//        List<String> allowedTypes = List.of("I1", "I2", "I3", "M1", "M2", "Doctoral School");
//        if (!allowedTypes.contains(value)) {
//            throw new RuntimeException("Invalid year type: " + value + ". Must be one of: " + allowedTypes);
//        }
//        var entity = repository.newEntity();
//
//        var isDuplicate = repository
//                .getAll()
//                .stream()
//                .anyMatch(studyYear -> studyYear.getValue().equals(value));
//        if (isDuplicate) throw new RuntimeException("StudyYear already exists");
//
//        entity.setValue(value);
//        repository.persist(entity);
//        return entity;
    }

    public StudyYear updateStudyYear(String id, Degree degree, String specialty, int max_years) {
        // TODO: Reimplement this :(
        throw new UnsupportedOperationException();
//        var entity = repository.getById(id);
//        List<String> allowedTypes = List.of("I1", "I2", "I3", "M1", "M2", "Doctoral School");
//        if (!allowedTypes.contains(value)) {
//            throw new RuntimeException("Invalid year type: " + value + ". Must be one of: " + allowedTypes);
//        }
//
//        var isDuplicate = repository
//                .getAll()
//                .stream()
//                .anyMatch(studyYear -> studyYear.getValue().equals(value));
//        if (isDuplicate) throw new RuntimeException("StudyYear already exists");
//
//        entity.setValue(value);
//        repository.merge(entity);
//
//        return entity;
    }

    @Override
    public void remove(String id) {
        // TODO: Implement this ( remember to delete entities from FacultyGroup )
        throw new UnsupportedOperationException();
    }
}
