package fii.css.database.persistence.entities;

import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setName("John Smith");
        teacher.setTitle("Professor");
    }

    @Test
    void testGettersAndSetters() {
        assertEquals("John Smith", teacher.getName());
        assertEquals("Professor", teacher.getTitle());
        assertNull(teacher.getId(), "ID should be null by default");
    }

    @Test
    void testCloneCreatesEqualCopy() throws Exception {
        // Use reflection to set private id
        Field idField = Teacher.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(teacher, "T123");

        Teacher copy = (Teacher) teacher.clone();

        assertNotSame(teacher, copy);
        assertEquals("T123", copy.getId());
        assertEquals(teacher.getName(), copy.getName());
        assertEquals(teacher.getTitle(), copy.getTitle());
    }

    @Test
    void testAnnotationsPresent() throws Exception {
        assertTrue(Teacher.class.isAnnotationPresent(Table.class));
        assertEquals("Teacher", Teacher.class.getAnnotation(Table.class).value());

        Field idField = Teacher.class.getDeclaredField("id");
        assertTrue(idField.isAnnotationPresent(Id.class));
        assertTrue(idField.isAnnotationPresent(Column.class));
        assertEquals("id", idField.getAnnotation(Column.class).value());

        Field nameField = Teacher.class.getDeclaredField("name");
        assertTrue(nameField.isAnnotationPresent(Column.class));
        assertEquals("name", nameField.getAnnotation(Column.class).value());

        Field titleField = Teacher.class.getDeclaredField("title");
        assertTrue(titleField.isAnnotationPresent(Column.class));
        assertEquals("title", titleField.getAnnotation(Column.class).value());
    }
}
