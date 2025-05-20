package fii.css.api.controllers;

import fii.css.api.Query;
import fii.css.database.Database;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.Discipline;
import fii.css.database.persistence.managers.DisciplineManager;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DisciplineControllerTest {
    private DisciplineController controller;
    private DisciplineManager mockManager;
    private Context mockContext;

    @BeforeEach
    void setUp() {
        mockManager = mock(DisciplineManager.class);
        mockContext = mock(Context.class);
        Database mockDatabase = mock(Database.class);

        when(mockDatabase.getDisciplineManager()).thenReturn(mockManager);

        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);
            controller = new DisciplineController() {
                @Override
                protected DisciplineManager getManager() {
                    return mockManager;
                }
            };
        }
    }

    @Test
    void testRegisterEndpoints() {
        Javalin mockApp = mock(Javalin.class);

        controller.registerEndpoints(mockApp);

        verify(mockApp).get(eq("/disciplines/{id}"), any());
        verify(mockApp).get(eq("/disciplines"), any());
        verify(mockApp).post(eq("/disciplines"), any());
        verify(mockApp).patch(eq("/disciplines/{id}"), any());
        verify(mockApp).delete(eq("/disciplines/{id}"), any());
    }

    @Test
    void testGet_ShouldReturnDiscipline_WhenFound() {
        Discipline discipline = new Discipline();
        when(mockManager.get(anyString())).thenReturn(discipline);
        when(mockContext.pathParam("id")).thenReturn("1");
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.get(mockContext);

        verify(mockContext).json(discipline);
        verify(mockContext).status(200);
    }

    @Test
    void testGet_ShouldReturn404_WhenDisciplineNotFound() {
        when(mockManager.get(anyString())).thenReturn(null);
        when(mockContext.pathParam("id")).thenReturn("1");

        controller.get(mockContext);

        verify(mockContext).status(404);
        verify(mockContext, never()).json(any());
    }

    @Test
    void testGetAll_ShouldReturnAllDisciplines() {
        List<Discipline> disciplines = List.of(mock(Discipline.class));
        when(mockManager.getAll()).thenReturn(disciplines);
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.getAll(mockContext);

        verify(mockContext).json(disciplines);
        verify(mockContext).status(200);
    }

    @Test
    void testCreate_ShouldCallAddDiscipline() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("name"))).thenReturn("Math");
            queryMock.when(() -> Query.degreeParam(eq(mockContext), eq("degree"))).thenReturn(Degree.Bachelor);
            queryMock.when(() -> Query.integerParam(eq(mockContext), eq("year"))).thenReturn(1);

            controller.create(mockContext);

            verify(mockManager).addDiscipline("Math", Degree.Bachelor, 1);
            verify(mockContext).status(201);
        }
    }

    @Test
    void testUpdate_ShouldCallUpdateDiscipline() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("name"))).thenReturn("Physics");
            queryMock.when(() -> Query.degreeParam(eq(mockContext), eq("degree"))).thenReturn(Degree.Master);
            queryMock.when(() -> Query.integerParam(eq(mockContext), eq("year"))).thenReturn(2);

            controller.update(mockContext);

            verify(mockManager).updateDiscipline("1", "Physics", Degree.Master, 2);
            verify(mockContext).status(204);
        }
    }

    @Test
    void testDelete_ShouldCallRemoveDiscipline() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");

            controller.delete(mockContext);

            verify(mockManager).remove("1");
            verify(mockContext).status(204);
        }
    }
}