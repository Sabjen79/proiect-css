package fii.css.database.persistence;

import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

import java.lang.reflect.Field;
import java.sql.SQLException;

public abstract class DatabaseEntity {
    public String getTableName() {
        return this.getClass().getAnnotation(Table.class).value();
    }

    public String getIdFromAnnotation() {
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);

                String value;

                try {
                    value = (String) field.get(this);
                } catch (IllegalAccessException e) {
                    field.setAccessible(false);
                    throw new RuntimeException(e);
                }

                field.setAccessible(false);

                return value;
            }
        }

        throw new RuntimeException("Could not find Id field");
    }
}
