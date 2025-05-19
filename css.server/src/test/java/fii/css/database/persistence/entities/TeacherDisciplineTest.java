package fii.css.database.persistence.entities;

import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherDisciplineTest {

    TeacherDiscipline td;

    @BeforeEach
    void setUp() {
        td = new TeacherDiscipline();
        td.setTeacherId("T001");
        td.setDisciplineId("D001");
    }

    @Test
    void testGettersAndSetters() {
        assertEquals("T001", td.getTeacherId());
        assertEquals("D001", td.getDisciplineId());
        assertNull(td.getId(), "ID should be null by default");
    }

    @Test
    void testCloneCreatesEqualCopy() throws Exception {
        // Use reflection to set private 'id'
        Field idField = TeacherDiscipline.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(td, "TD123");

        TeacherDiscipline copy = (TeacherDiscipline) td.clone();

        assertNotSame(td, copy);
        assertEquals("TD123", copy.getId());
        assertEquals(td.getTeacherId(), copy.getTeacherId());
        assertEquals(td.getDisciplineId(), copy.getDisciplineId());
    }

    @Test
    void testAnnotationsPresent() throws Exception {
        assertTrue(TeacherDiscipline.class.isAnnotationPresent(Table.class));
        assertEquals("TeacherDiscipline", TeacherDiscipline.class.getAnnotation(Table.class).value());

        Field idField = TeacherDiscipline.class.getDeclaredField("id");
        assertTrue(idField.isAnnotationPresent(Id.class));
        assertTrue(idField.isAnnotationPresent(Column.class));
        assertEquals("id", idField.getAnnotation(Column.class).value());

        Field teacherIdField = TeacherDiscipline.class.getDeclaredField("teacherId");
        assertTrue(teacherIdField.isAnnotationPresent(Column.class));
        assertEquals("teacher_id", teacherIdField.getAnnotation(Column.class).value());

        Field disciplineIdField = TeacherDiscipline.class.getDeclaredField("disciplineId");
        assertTrue(disciplineIdField.isAnnotationPresent(Column.class));
        assertEquals("discipline_id", disciplineIdField.getAnnotation(Column.class).value());
    }
}
