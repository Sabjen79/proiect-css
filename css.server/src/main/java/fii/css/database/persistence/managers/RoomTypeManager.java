package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.RoomType;
import fii.css.database.persistence.repositories.RoomTypeRepository;

import java.util.List;

public class RoomTypeManager extends AbstractEntityManager<RoomType>{
    public RoomTypeManager() {super(new RoomTypeRepository());}

    public RoomType addRoomType(String typeName) {
        // inspired by the actual timetable
        List<String> allowedTypes = List.of("Seminary", "Laboratory", "Course", "Office", "Equipment");

        if (!allowedTypes.contains(typeName)) {
            throw new RuntimeException("Invalid room type: " + typeName + ". Must be one of: " + allowedTypes);
        }
        var entity = repository.newEntity();

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(roomType -> roomType.getTypeName().equals(typeName));

        if (isDuplicate) throw new RuntimeException("Room type with name " + typeName + " already exists");

        entity.setTypeName(typeName);

        repository.persist(entity);
        return entity;
    }

    public RoomType updateRoomType(int id, String typeName) {
        var entity = repository.getById(id);

        List<String> allowedTypes = List.of("Seminary", "Laboratory", "Course", "Office", "Equipment");

        if (!allowedTypes.contains(typeName)) {
            throw new RuntimeException("Invalid room type: " + typeName + ". Must be one of: " + allowedTypes);
        }

        var isDuplicate = repository
                .getAll()
                .stream()
                .anyMatch(roomType -> roomType.getTypeName().equals(typeName));

        if (isDuplicate) throw new RuntimeException("Room type with name " + typeName + " already exists");

        entity.setTypeName(typeName);
        repository.merge(entity);
        return entity;
    }

    public void removeRoomType(int id) {repository.delete(repository.getById(id));}

}
