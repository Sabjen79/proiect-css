
package fii.css.api;

import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Degree;
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
    void testDegreeParam_withValidValue() {
        Context mockContext = Mockito.mock(Context.class);
        when(mockContext.formParam("degree")).thenReturn("0");

        Degree result = Query.degreeParam(mockContext, "degree");
        assertEquals(Degree.Bachelor, result);
    }
}