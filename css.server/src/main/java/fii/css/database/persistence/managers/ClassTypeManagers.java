package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.ClassType;
import fii.css.database.persistence.repositories.AbstractRepository;

import java.util.List;

public class ClassTypeManagers extends AbstractEntityManager<ClassType> {
    public ClassTypeManagers(AbstractRepository<ClassType> repo) {
        super(repo);
    }

    public ClassType addClassType(String typeName) {
        List<String> allowedTypes = List.of("Course", "Laboratory", "Seminary");

        if(!allowedTypes.contains(typeName)) {
            throw new RuntimeException("Invalid class type: " + typeName + ". Must be one of: " + allowedTypes);
        }

        var entity = repository.newEntity();

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(classType -> classType.getTypeName().equals(typeName));

        if (isDuplicate) throw new RuntimeException("Class type with name " + typeName + " already exists");

        entity.setTypeName(typeName);

        repository.persist(entity);
        return entity;
    }

    public ClassType updateClassType(int id, String typeName) {
        var entity = repository.getById(id);

        List<String> allowedTypes = List.of("Course", "Laboratory", "Seminary");

        if (!allowedTypes.contains(typeName)) {
            throw new RuntimeException("Invalid class type: " + typeName + ". Must be one of: " + allowedTypes);
        }

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(roomType -> roomType.getTypeName().equals(typeName));

        if (isDuplicate) throw new RuntimeException("Class type with name " + typeName + " already exists");

        entity.setTypeName(typeName);
        repository.merge(entity);
        return entity;
    }

    public void removeRoomType(int id) {repository.delete(repository.getById(id));}
}
