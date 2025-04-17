package fii.css.database.persistence.repositories;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.RandomId;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository<T extends DatabaseEntity> {
    protected final List<T> entities;
    private final Class<T> clazz;

    AbstractRepository(Class<T> clazz) {
        try {
            this.clazz = clazz;

            this.entities = new ArrayList<>();

            var tableName = clazz.getAnnotation(Table.class).value();

            var connection = Database.getInstance().getConnection();

            var resultSet = connection.createStatement()
                    .executeQuery("SELECT * FROM " + tableName);

            while(resultSet.next()) {
                entities.add(fromResultSet(resultSet));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private T fromResultSet(ResultSet rs) {
        try {
            T entity = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);

                    var columnName = field.getAnnotation(Column.class).value();

                    field.set(entity, rs.getObject(columnName));

                    field.setAccessible(false);
                }
            }

            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    public T getById(int id) {
        for (T entity : getAll()) {
            if(entity.getIdFromAnnotation() == id) {
                return entity;
            }
        }

        return null;
    }

    /// Returns a COPY of all entities. Modifications should be persisted manually.
    public List<T> getAll() {
        return List.copyOf(entities);
    }

    public T newEntity() {
        try {
            var entity = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);

                    field.set(entity, RandomId.newId());

                    field.setAccessible(false);
                }
            }

            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void persist(T entity) {
        try {
            if(getById(entity.getIdFromAnnotation()) != null) {
                throw new RuntimeException("Existing entities should be merged, not persisted.");
            }

            var table = entity.getTableName();

            var columnQuery = new StringBuilder();
            var valueQuery = new StringBuilder();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);

                    var columnName = field.getAnnotation(Column.class).value();
                    if(!columnQuery.isEmpty()) columnQuery.append(",");
                    columnQuery.append(columnName);

                    var value = field.get(entity);
                    if(value == null) throw new RuntimeException("Field " + field.getName() + " has null value.");

                    if(!valueQuery.isEmpty()) valueQuery.append(",");
                    valueQuery.append("'")
                            .append(value)
                            .append("'");

                    field.setAccessible(false);
                }
            }

            var connection = Database.getInstance().getConnection();
            var stmt = connection.createStatement();

            stmt.execute(
                    String.format("INSERT INTO %s (%s) VALUES (%s)", table, columnQuery, valueQuery)
            );

            if(!entities.contains(entity)) entities.add(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// Updates an entity to the database and to the entity list
    public void merge(T updatedEntity) {
        try {
            var table = updatedEntity.getTableName();
            var id = updatedEntity.getIdFromAnnotation();
            String idColumn = null;

            var partial_query = new StringBuilder();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);

                    var columnName = field.getAnnotation(Column.class).value();
                    if(field.isAnnotationPresent(Id.class)) {
                        idColumn = columnName;
                    }

                    if(!partial_query.isEmpty()) partial_query.append(",");

                    var value = field.get(updatedEntity);

                    if(value == null) throw new RuntimeException("Field " + field.getName() + " has null value.");

                    partial_query.append(columnName)
                            .append(" = '")
                            .append(value)
                            .append("'");

                    field.setAccessible(false);
                }
            }

            if(idColumn == null) throw new RuntimeException("Id should contain both @Id and @Column");

            var connection = Database.getInstance().getConnection();
            var stmt = connection.createStatement();

            stmt.execute(
                    String.format("UPDATE %s SET %s WHERE %s = %s", table, partial_query, idColumn, id)
            );

            entities.set(entities.indexOf(getById(id)), updatedEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(T entity) {
        try {
            var table = entity.getTableName();
            var id = entity.getIdFromAnnotation();
            String idColumn = null;

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);

                    idColumn = field.getAnnotation(Column.class).value();

                    field.setAccessible(false);
                }
            }

            if(idColumn == null) throw new RuntimeException("Id should contain both @Id and @Column");

            var connection = Database.getInstance().getConnection();
            var stmt = connection.createStatement();

            stmt.execute(
                    String.format("DELETE FROM %s WHERE %s = %s", table, idColumn, id)
            );

            entities.remove(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
