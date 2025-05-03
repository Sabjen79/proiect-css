package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.StudyYear;
import fii.css.database.persistence.repositories.StudyYearRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class StudyYearManager extends AbstractEntityManager<StudyYear> {
    // TODO: add others necessary here, I couldn't think of anything else for now :))
    private static final Set<String> MASTER_SPECIALTIES = Set.of("ISS", "SD", "IAO", "SI", "SAI", "MLC");

    public StudyYearManager() {super(new StudyYearRepository());}

    @Override
    public StudyYear get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<StudyYear> getAll() {
        return repository.getAll();
    }

    public StudyYear addStudyYear(Degree degree, String specialty, int maxYears) {
        validateStudyYear(degree, specialty, maxYears);

        StudyYear entity = repository.newEntity();
        entity.setDegree(degree);
        entity.setSpecialty(specialty);
        entity.setMaxYears(maxYears);

        repository.persist(entity);
        return entity;
    }

    public StudyYear updateStudyYear(String id, Degree degree, String specialty, int maxYears) {
        StudyYear entity = repository.getById(id);
        if (entity == null) {
            throw new RuntimeException("Study year with ID " + id + " does not exist.");
        }

        validateStudyYear(degree, specialty, maxYears);

        entity.setDegree(degree);

        //poti face update doar daca specializarea ramane aceeasi
        if(!Objects.equals(entity.getSpecialty(), specialty)){
            throw new RuntimeException("Can't update study year from specialty '" + entity.getSpecialty() + "' to '" + specialty+ "'.");
        }else{
            entity.setSpecialty(specialty);
        }
        entity.setMaxYears(maxYears);

        repository.merge(entity);
        return entity;
    }

    @Override
    public void remove(String id) {
        StudyYear studyYear = repository.getById(id);
        if (studyYear == null) {
            throw new RuntimeException("Study year with ID " + id + " does not exist.");
        }

        // delete associated faculty groups
        try {
            var connection = fii.css.database.Database.getInstance().getConnection();
            var stmt = connection.prepareStatement("DELETE FROM FacultyGroup WHERE study_year_id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete faculty groups for study year with ID " + id, e);
        }

        // delete the study year
        repository.delete(studyYear);
    }

    private void validateStudyYear(Degree degree, String specialty, int maxYears) {

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(studyYear -> studyYear.getSpecialty().equals(specialty) && studyYear.getDegree().equals(degree));

        if (isDuplicate) throw new RuntimeException("Can't add study year again.");

        if (degree == Degree.Bachelor) {
            if (!specialty.equalsIgnoreCase("Computer Science")) {
                throw new RuntimeException("Bachelor degree must have specialty 'Computer Science'.");
            }
            if (maxYears != 3) {
                throw new RuntimeException("Bachelor degree must have exactly 3 years.");
            }
        } else if (degree == Degree.Master) {
            if (!MASTER_SPECIALTIES.contains(specialty)) {
                throw new RuntimeException("Master degree must have a specialty from: " + MASTER_SPECIALTIES);
            }
            if (maxYears != 2) {
                throw new RuntimeException("Master degree must have exactly 2 years.");
            }
        } else if (degree == Degree.PhD) {
            // TODO
            // i suppose that phd can have any specialty and any number of years
            // if im wrong, please correct me
            if (maxYears <= 0) {
                throw new RuntimeException("PhD degree must have a positive number of years.");
            }
        } else {
            throw new RuntimeException("Unsupported degree type: " + degree);
        }
    }
}
