package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;
import fii.css.database.persistence.managers.FacultyGroupManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SemiYearTest {

    SemiYear semiYear;

    @BeforeEach
    void setUp() {
        semiYear = new SemiYear();
        semiYear.setName("A");
        semiYear.setDegree(Degree.Bachelor);
        semiYear.setYear(1);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals("A", semiYear.getName());
        assertEquals(Degree.Bachelor, semiYear.getDegree());
        assertEquals(1, semiYear.getYear());
    }

    @Test
    void testCloneCreatesEqualCopy() throws Exception {
        // Set private id via reflection
        Field idField = SemiYear.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(semiYear, "SY1");

        SemiYear copy = (SemiYear) semiYear.clone();

        assertNotSame(semiYear, copy);
        assertEquals("SY1", copy.getId());
        assertEquals(semiYear.getName(), copy.getName());
        assertEquals(semiYear.getDegree(), copy.getDegree());
        assertEquals(semiYear.getYear(), copy.getYear());
    }


    @Test
    void testAnnotationsPresent() throws Exception {
        assertTrue(SemiYear.class.isAnnotationPresent(Table.class));
        assertEquals("SemiYear", SemiYear.class.getAnnotation(Table.class).value());

        Field idField = SemiYear.class.getDeclaredField("id");
        assertTrue(idField.isAnnotationPresent(Id.class));
        assertTrue(idField.isAnnotationPresent(Column.class));
        assertEquals("id", idField.getAnnotation(Column.class).value());

        Field nameField = SemiYear.class.getDeclaredField("name");
        assertTrue(nameField.isAnnotationPresent(Column.class));
        assertEquals("name", nameField.getAnnotation(Column.class).value());

        Field degreeField = SemiYear.class.getDeclaredField("degree");
        assertTrue(degreeField.isAnnotationPresent(Column.class));
        assertEquals("degree", degreeField.getAnnotation(Column.class).value());

        Field yearField = SemiYear.class.getDeclaredField("year");
        assertTrue(yearField.isAnnotationPresent(Column.class));
        assertEquals("year", yearField.getAnnotation(Column.class).value());
    }
}
