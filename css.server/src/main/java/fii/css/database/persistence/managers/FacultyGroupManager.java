package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.FacultyGroup;
import fii.css.database.persistence.entities.StudyYear;
import fii.css.database.persistence.repositories.FacultyGroupRepository;

import java.util.List;
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

    public void addFacultyGroup(String name, int year, String studyYearId) {
        FacultyGroup entity = repository.newEntity();

        entity.setName(name.trim());
        entity.setYear(year);
        entity.setStudyYearId(studyYearId.trim());

        validate(entity);

        repository.persist(entity);
    }

    public void updateFacultyGroup(String id, String name, int year, String studyYearId) {
        FacultyGroup entity = repository.getById(id);
        
        if (entity == null) {
            throw new DatabaseException("Faculty group with ID " + id + " not found.");
        }

        entity.setName(name.trim());
        entity.setYear(year);
        entity.setStudyYearId(studyYearId.trim());

        validate(entity);

        repository.merge(entity);
    }

    @Override
    public void remove(String id) {
        FacultyGroup facultyGroup = repository.getById(id);
        
        if (facultyGroup == null) {
            throw new DatabaseException("Faculty group with ID " + id + " does not exist.");
        }

        var sManager = Database.getInstance().scheduleManager;
        sManager.getAll().forEach(s -> {
            if(s.getFacultyGroup().getId().equals(id)) {
                sManager.remove(s.getId());
            }
        });

        repository.delete(facultyGroup);
    }
    
    private void validate(FacultyGroup fg) {
        var studyYear = Database.getInstance().studyYearManager.get(fg.getStudyYear().getId());
        
        if(studyYear == null) {
            throw new DatabaseException("Study year with ID " + fg.getStudyYear().getId() + " does not exist.");
        }

        if(fg.getGroupName().length() != 2) {
            throw new DatabaseException("Faculty group name must have 2 characters.");
        }

        String prefix = fg.getGroupName().substring(0, 1).toUpperCase();
        String numberPart = fg.getGroupName().substring(1);

        if (!ALLOWED_PREFIXES.contains(prefix)) {
            throw new DatabaseException("Group name must start with one of " + ALLOWED_PREFIXES);
        }

        try {
            Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            throw new DatabaseException("Second character of group name must be a number (0-9).");
        }

        if(fg.getYear() < 1 || fg.getYear() > studyYear.getMaxYears()) {
            throw new DatabaseException("Study year must be between 1 and " + (studyYear.getMaxYears()));
        }

        for(var group : repository.getAll()) {
            if(!group.getId().equals(fg.getId())
                    && group.getGroupName().equalsIgnoreCase(fg.getGroupName())
                    && group.getStudyYear().getId().equalsIgnoreCase(fg.getStudyYear().getId())) {
                throw new DatabaseException("Faculty group already exists for this study year.");
            }
        }
    }
}
