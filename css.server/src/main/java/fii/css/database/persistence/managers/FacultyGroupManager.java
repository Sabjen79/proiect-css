package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.FacultyGroup;
import fii.css.database.persistence.entities.StudyYear;
import fii.css.database.persistence.repositories.FacultyGroupRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FacultyGroupManager extends AbstractEntityManager<FacultyGroup> {
    private static final Set<String> ALLOWED_PREFIXES = Set.of("A", "B", "E", "X");
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
        validateFacultyGroup(name, year, studyYear);

        // check for duplicated
        boolean duplicate = repository.getAll().stream()
                .anyMatch(group -> group.getGroupName().equalsIgnoreCase(name)
                        && group.getStudyYear().getId().equals(studyYear.getId()));

        if (duplicate) {
            throw new RuntimeException("Faculty group '" + name + "' already exists for the specified study year.");
        }

        FacultyGroup entity = repository.newEntity();
        entity.setGroupName(name);
        entity.setYear(year);

        //daca numele grupei e de 2 litere => licenta, daca nu e master
        if(name.length() == 2 && studyYear.getSpecialty().equals("Computer Science")){
            entity.setStudyYear(studyYear);
        }else if(name.length() == 3 && studyYear.getSpecialty().length()>=3){
            entity.setStudyYear(studyYear);
        }else {
            throw new RuntimeException("Can't combine bachelor and masters.");
        }


        repository.persist(entity);
        return entity;
    }


    public FacultyGroup updateFacultyGroup(String id, String name, int year, StudyYear studyYear) {
        FacultyGroup entity = repository.getById(id);
        if (entity == null) {
            throw new RuntimeException("Faculty group with ID " + id + " not found.");
        }

        validateFacultyGroup(name, year, studyYear);

        boolean duplicate = repository.getAll().stream()
                .anyMatch(group -> !group.getId().equals(id) &&
                        group.getGroupName().equalsIgnoreCase(name) &&
                        group.getStudyYear().getId().equals(studyYear.getId()));

        if (duplicate) {
            throw new RuntimeException("Another faculty group with name '" + name + "' already exists for the specified study year.");
        }

        entity.setGroupName(name);
        entity.setYear(year);
        entity.setStudyYear(studyYear);

        repository.merge(entity);
        return entity;
    }

    @Override
    public void remove(String id) {
        FacultyGroup facultyGroup = repository.getById(id);
        if (facultyGroup == null) {
            throw new RuntimeException("Faculty group with ID " + id + " does not exist.");
        }

        // delete schedules from this faculty group
        try {
            var connection = Database.getInstance().getConnection();
            var stmt = connection.prepareStatement("DELETE FROM Schedule WHERE faculty_group_id = ?");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete schedules for faculty group with ID " + id, e);
        }

        repository.delete(facultyGroup);
    }
    private void validateFacultyGroup(String name, int year, StudyYear studyYear) {
        if (name.length() != 2) {
            throw new RuntimeException("Group name must have exactly 2 characters.");
        }

        String prefix = name.substring(0, 1).toUpperCase();
        String numberPart = name.substring(1);

        if (!ALLOWED_PREFIXES.contains(prefix)) {
            throw new RuntimeException("Group name must start with one of " + ALLOWED_PREFIXES);
        }

        try {
            Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Second character of group name must be a number (0-9).");
        }

        Degree degree = studyYear.getDegree();
        if (degree == Degree.Bachelor && (year < 1 || year > studyYear.getMaxYears())) {
            throw new RuntimeException("Bachelor group must have year between 1 and 3.");
        } else if (degree == Degree.Master && (year < 1 || year > studyYear.getMaxYears())) {
            throw new RuntimeException("Master group must have year between 1 and 2.");
        } else if (degree == Degree.PhD) {
            // TODO
            // i suppose phd doesn't have year restrictions?
        }
    }
}
