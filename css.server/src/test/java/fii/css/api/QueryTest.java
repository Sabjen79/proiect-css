
package fii.css.api;

import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.ClassType;
import fii.css.database.persistence.entities.DayOfWeek;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.RoomType;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class QueryTest {

    @Test
    void testIntegerParam_validInput() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("age")).thenReturn("25");

        int result = Query.integerParam(mockContext, "age");
        assertEquals(25, result);
    }

    @Test
    void testIntegerParam_invalidInput() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("age")).thenReturn("invalid");

        DatabaseException exception = assertThrows(DatabaseException.class,
                () -> Query.integerParam(mockContext, "age")
        );
        assertEquals("'age' should be an integer", exception.getMessage());
    }

    @Test
    void testIntegerParam_missingParam() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("age")).thenReturn(null);

        DatabaseException exception = assertThrows(DatabaseException.class,
                () -> Query.integerParam(mockContext, "age")
        );
        assertEquals("Query parameter 'age' not found", exception.getMessage());
    }

    @Test
    void testStringParam_validInput() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("name")).thenReturn("Alice");

        String result = Query.stringParam(mockContext, "name");
        assertEquals("Alice", result);
    }

    @Test
    void testStringParam_emptyInput() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("name")).thenReturn("");

        DatabaseException exception = assertThrows(DatabaseException.class,
                () -> Query.stringParam(mockContext, "name")
        );
        assertEquals("'name' cannot be empty", exception.getMessage());
    }

    @Test
    void testStringParam_nullInput() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("name")).thenReturn("");

        assertThrows(DatabaseException.class, () -> Query.stringParam(mockContext, "???"));
    }

    @Test
    void testDegreeParam_withValidValue() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("degree")).thenReturn("0");

        Degree result = Query.degreeParam(mockContext, "degree");
        assertEquals(Degree.Bachelor, result);
    }

    @Test
    void testIdParam_withValidValue() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.pathParam("id")).thenReturn("id");

        String result = Query.idPathParam(mockContext);
        assertEquals("id", result);
    }

    @Test
    void testClassTypeParam_withValidValue() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("classtype")).thenReturn("0");

        var result = Query.classTypeParam(mockContext, "classtype");
        assertEquals(ClassType.Course, result);
    }

    @Test
    void testClassTypeParam_withInvalidValue() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("classtype")).thenReturn("5");

        assertThrows(DatabaseException.class, () -> Query.classTypeParam(mockContext, "classtype"));
    }

    @Test
    void testDayOfWeekParam_withValidValue() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("day")).thenReturn("0");

        var result = Query.dayOfWeekParam(mockContext, "day");
        assertEquals(DayOfWeek.Monday, result);
    }

    @Test
    void testDayOfWeekParam_withInvalidValue() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("day")).thenReturn("5");

        assertThrows(DatabaseException.class, () -> Query.dayOfWeekParam(mockContext, "day"));
    }

    @Test
    void testRoomTypeParam_withValidValue() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("roomtype")).thenReturn("0");

        var result = Query.roomTypeParam(mockContext, "roomtype");
        assertEquals(RoomType.Course, result);
    }

    @Test
    void testRoomTypeParam_withInvalidValue() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("roomtype")).thenReturn("5");

        assertThrows(DatabaseException.class, () -> Query.roomTypeParam(mockContext, "roomtype"));
    }
}