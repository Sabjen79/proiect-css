package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.FacultyGroup;
import fii.css.database.persistence.repositories.AbstractRepository;
import fii.css.database.persistence.repositories.FacultyGroupRepository;

import java.util.List;
import java.util.Set;

public class FacultyGroupManager extends AbstractEntityManager<FacultyGroup> {
    
    public FacultyGroupManager(AbstractRepository<FacultyGroup> repository) {
        super(repository);
    }

    @Override
    public FacultyGroup get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<FacultyGroup> getAll() {
        return repository.getAll();
    }

    public void addFacultyGroup(String name, String semiYearId) {
        FacultyGroup entity = repository.newEntity();

        entity.setName(name.trim());
        entity.setSemiYearId(semiYearId.trim());

        validate(entity);

        repository.persist(entity);
    }

    public void updateFacultyGroup(String id, String name, String semiYearId) {
        FacultyGroup entity = get(id);
        
        if (entity == null) {
            throw new DatabaseException("Faculty group with ID " + id + " not found.");
        }

        entity.setName(name.trim());
        entity.setSemiYearId(semiYearId.trim());

        validate(entity);

        repository.merge(entity);
    }

    @Override
    public void remove(String id) {
        FacultyGroup facultyGroup = get(id);
        
        if (facultyGroup == null) {
            throw new DatabaseException("Faculty group with ID " + id + " does not exist.");
        }

        var sManager = Database.getInstance().getScheduleManager();
        sManager.getAll().forEach(s -> {
            if(s.getGroup().getIdFromAnnotation().equals(id)) {
                throw new DatabaseException("Group is still referenced in schedule.");
            }
        });

        repository.delete(facultyGroup);
    }
    
    private void validate(FacultyGroup fg) {
        assert fg != null;
        assert fg.getId() != null;
        assert fg.getName() != null && !fg.getName().isBlank();
        assert fg.getSemiYearId() != null && !fg.getSemiYearId().isBlank();

        var semiyear = Database.getInstance().getSemiYearManager().get(fg.getSemiYearId());
        
        if(semiyear == null) {
            throw new DatabaseException("Semi-year with ID " + fg.getSemiYearId() + " does not exist.");
        }

        for(var group : getAll()) {
            if(!group.getId().equals(fg.getId())
                    && group.getName().equalsIgnoreCase(fg.getName())
                    && group.getSemiYearId().equalsIgnoreCase(fg.getSemiYearId())) {
                throw new DatabaseException("Faculty group already exists for this semi-year.");
            }
        }

        for(var s : Database.getInstance().getScheduleManager().getAll()) {
            if(s.getGroup() instanceof FacultyGroup group) {
                if(group.getId().equals(fg.getId())) {
                    throw new DatabaseException("Semi-year cannot be changed while this faculty group is still referenced in schedule.");
                }
            }
        }
    }
}
