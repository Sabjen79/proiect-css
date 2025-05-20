package fii.css.api.controllers;

import fii.css.api.Query;
import fii.css.database.Database;
import fii.css.database.persistence.entities.TeacherDiscipline;
import fii.css.database.persistence.managers.TeacherDisciplineManager;
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

class TeacherDisciplineControllerTest {
    private TeacherDisciplineController controller;
    private TeacherDisciplineManager mockManager;
    private Context mockContext;

    @BeforeEach
    void setUp() {
        mockManager = mock(TeacherDisciplineManager.class);
        mockContext = mock(Context.class);
        Database mockDatabase = mock(Database.class);

        when(mockDatabase.getTeacherDisciplineManager()).thenReturn(mockManager);

        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);
            controller = new TeacherDisciplineController();
        }
    }

    @Test
    void testRegisterEndpoints() {
        Javalin mockApp = mock(Javalin.class);

        controller.registerEndpoints(mockApp);

        verify(mockApp).get(eq("/teacherDisciplines/{id}"), any());
        verify(mockApp).get(eq("/teacherDisciplines"), any());
        verify(mockApp).post(eq("/teacherDisciplines"), any());
        verify(mockApp).delete(eq("/teacherDisciplines/{id}"), any());
    }


    @Test
    void testGet_ShouldReturnTeacherDiscipline_WhenFound() {
        TeacherDiscipline teacherDiscipline = new TeacherDiscipline();
        when(mockManager.get(anyString())).thenReturn(teacherDiscipline);
        when(mockContext.pathParam("id")).thenReturn("1");
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.get(mockContext);

        verify(mockContext).json(teacherDiscipline);
        verify(mockContext).status(200);
    }

    @Test
    void testGet_ShouldReturn404_WhenTeacherDisciplineNotFound() {
        when(mockManager.get(anyString())).thenReturn(null);
        when(mockContext.pathParam("id")).thenReturn("1");

        controller.get(mockContext);

        verify(mockContext).status(404);
        verify(mockContext, never()).json(any());
    }

    @Test
    void testGetAll_ShouldReturnAllTeacherDisciplines() {
        List<TeacherDiscipline> teacherDisciplines = List.of(mock(TeacherDiscipline.class));
        when(mockManager.getAll()).thenReturn(teacherDisciplines);
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.getAll(mockContext);

        verify(mockContext).json(teacherDisciplines);
        verify(mockContext).status(200);
    }

    @Test
    void testCreate_ShouldCallAddTeacherDiscipline() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("teacherId"))).thenReturn("T1");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("disciplineId"))).thenReturn("D1");

            controller.create(mockContext);

            verify(mockManager).addTeacherDiscipline("T1", "D1");
            verify(mockContext).status(201);
        }
    }

    @Test
    void testDelete_ShouldCallRemoveTeacherDiscipline() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");

            controller.delete(mockContext);

            verify(mockManager).remove("1");
            verify(mockContext).status(204);
        }
    }
}