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
    }

    public StudyYear updateStudyYear(String id, Degree degree, String specialty, int max_years) {
        // TODO: Reimplement this :(
        throw new UnsupportedOperationException();

    }

    @Override
    public void remove(String id) {
        // TODO: Implement this ( remember to delete entities from FacultyGroup )
        throw new UnsupportedOperationException();
    }
}
