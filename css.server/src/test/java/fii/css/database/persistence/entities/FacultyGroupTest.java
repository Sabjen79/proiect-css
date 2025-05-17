package fii.css.database.persistence.entities;

import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class FacultyGroupTest {

    @Test
    void testGettersAndSetters() {
        FacultyGroup group = new FacultyGroup();

        group.setName("A1");
        group.setSemiYearId("SEMI001");

        assertNull(group.getId(), "ID should be null by default");
        assertEquals("A1", group.getName());
        assertEquals("SEMI001", group.getSemiYearId());
    }

    @Test
    void testCloneCreatesEqualCopy() {
        FacultyGroup original = new FacultyGroup();
        original.setName("A1");
        original.setSemiYearId("A");

        // Use reflection to set private ID field
        try {
            Field idField = FacultyGroup.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(original, "A2");
        } catch (Exception e) {
            fail("Failed to set private field via reflection: " + e.getMessage());
        }

        FacultyGroup cloned = (FacultyGroup) original.clone();

        assertNotSame(original, cloned, "Clone should return a new object");
        assertEquals(original.getId(), cloned.getId());
        assertEquals(original.getName(), cloned.getName());
        assertEquals(original.getSemiYearId(), cloned.getSemiYearId());
    }

    @Test
    void testAnnotationsPresence() throws Exception {
        assertTrue(FacultyGroup.class.isAnnotationPresent(Table.class), "Class should be annotated with @Table");
        Table table = FacultyGroup.class.getAnnotation(Table.class);
        assertEquals("FacultyGroup", table.value());

        Field idField = FacultyGroup.class.getDeclaredField("id");
        assertTrue(idField.isAnnotationPresent(Id.class));
        assertTrue(idField.isAnnotationPresent(Column.class));
        assertEquals("id", idField.getAnnotation(Column.class).value());

        Field nameField = FacultyGroup.class.getDeclaredField("name");
        assertTrue(nameField.isAnnotationPresent(Column.class));
        assertEquals("name", nameField.getAnnotation(Column.class).value());

        Field semiYearIdField = FacultyGroup.class.getDeclaredField("semiYearId");
        assertTrue(semiYearIdField.isAnnotationPresent(Column.class));
        assertEquals("semi_year_id", semiYearIdField.getAnnotation(Column.class).value());
    }

    @Test
    void testClonePreservesValues() {
        FacultyGroup original = new FacultyGroup();
        original.setName("B1");
        original.setSemiYearId("SEMI002");

        // Set ID via reflection
        try {
            Field idField = FacultyGroup.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(original, "FG002");

            FacultyGroup clone = (FacultyGroup) original.clone();

            assertEquals("FG002", clone.getId());
            assertEquals("B1", clone.getName());
            assertEquals("SEMI002", clone.getSemiYearId());

        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }
}
