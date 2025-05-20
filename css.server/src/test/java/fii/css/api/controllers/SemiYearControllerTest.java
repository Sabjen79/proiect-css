package fii.css.api.controllers;

import fii.css.api.Query;
import fii.css.database.Database;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.SemiYear;
import fii.css.database.persistence.managers.SemiYearManager;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SemiYearControllerTest {
    private SemiYearController controller;
    private SemiYearManager mockManager;
    private Context mockContext;

    @BeforeEach
    void setUp() {
        mockManager = mock(SemiYearManager.class);
        mockContext = mock(Context.class);
        Database mockDatabase = mock(Database.class);

        when(mockDatabase.getSemiYearManager()).thenReturn(mockManager);

        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);
            controller = new SemiYearController();
        }
    }

    @Test
    void testRegisterEndpoints() {
        Javalin mockApp = mock(Javalin.class);

        controller.registerEndpoints(mockApp);

        verify(mockApp).get(eq("/semiYears/{id}"), any());
        verify(mockApp).get(eq("/semiYears"), any());
        verify(mockApp).post(eq("/semiYears"), any());
        verify(mockApp).patch(eq("/semiYears/{id}"), any());
        verify(mockApp).delete(eq("/semiYears/{id}"), any());
    }


    @Test
    void testGet_ShouldReturnSemiYear_WhenFound() {
        SemiYear semiYear = new SemiYear();
        when(mockManager.get(anyString())).thenReturn(semiYear);
        when(mockContext.pathParam("id")).thenReturn("1");
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.get(mockContext);

        verify(mockContext).json(semiYear);
        verify(mockContext).status(200);
    }

    @Test
    void testGet_ShouldReturn404_WhenSemiYearNotFound() {
        when(mockManager.get(anyString())).thenReturn(null);
        when(mockContext.pathParam("id")).thenReturn("1");

        controller.get(mockContext);

        verify(mockContext).status(404);
        verify(mockContext, never()).json(any());
    }

    @Test
    void testGetAll_ShouldReturnAllSemiYears() {
        List<SemiYear> semiYears = List.of(mock(SemiYear.class));
        when(mockManager.getAll()).thenReturn(semiYears);
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.getAll(mockContext);

        verify(mockContext).json(semiYears);
        verify(mockContext).status(200);
    }

    @Test
    void testCreate_ShouldCallAddStudyYear() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("name"))).thenReturn("Test Year");
            queryMock.when(() -> Query.degreeParam(eq(mockContext), eq("degree"))).thenReturn(Degree.Bachelor);
            queryMock.when(() -> Query.integerParam(eq(mockContext), eq("year"))).thenReturn(2);

            controller.create(mockContext);

            verify(mockManager).addStudyYear("Test Year", Degree.Bachelor, 2);
            verify(mockContext).status(201);
        }
    }

    @Test
    void testUpdate_ShouldCallUpdateStudyYear() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("name"))).thenReturn("Updated Year");
            queryMock.when(() -> Query.degreeParam(eq(mockContext), eq("degree"))).thenReturn(Degree.Master);
            queryMock.when(() -> Query.integerParam(eq(mockContext), eq("year"))).thenReturn(1);

            controller.update(mockContext);

            verify(mockManager).updateStudyYear("1", "Updated Year", Degree.Master, 1);
            verify(mockContext).status(204);
        }
    }

    @Test
    void testDelete_ShouldCallRemoveSemiYear() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");

            controller.delete(mockContext);

            verify(mockManager).remove("1");
            verify(mockContext).status(204);
        }
    }
}