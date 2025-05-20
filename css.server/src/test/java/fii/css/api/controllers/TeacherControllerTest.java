package fii.css.api.controllers;

import fii.css.api.Query;
import fii.css.database.Database;
import fii.css.database.persistence.entities.Teacher;
import fii.css.database.persistence.managers.TeacherManager;
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

class TeacherControllerTest {
    private TeacherController controller;
    private TeacherManager mockManager;
    private Context mockContext;

    @BeforeEach
    void setUp() {
        mockManager = mock(TeacherManager.class);
        mockContext = mock(Context.class);
        Database mockDatabase = mock(Database.class);

        when(mockDatabase.getTeacherManager()).thenReturn(mockManager);

        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);
            controller = new TeacherController();
        }
    }

    @Test
    void testRegisterEndpoints() {
        Javalin mockApp = mock(Javalin.class);

        controller.registerEndpoints(mockApp);

        verify(mockApp).get(eq("/teachers/{id}"), any());
        verify(mockApp).get(eq("/teachers"), any());
        verify(mockApp).post(eq("/teachers"), any());
        verify(mockApp).patch(eq("/teachers/{id}"), any());
        verify(mockApp).delete(eq("/teachers/{id}"), any());
    }


    @Test
    void testGet_ShouldReturnTeacher_WhenFound() {
        Teacher teacher = new Teacher();
        when(mockManager.get(anyString())).thenReturn(teacher);
        when(mockContext.pathParam("id")).thenReturn("1");
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.get(mockContext);

        verify(mockContext).json(teacher);
        verify(mockContext).status(200);
    }

    @Test
    void testGet_ShouldReturn404_WhenTeacherNotFound() {
        when(mockManager.get(anyString())).thenReturn(null);
        when(mockContext.pathParam("id")).thenReturn("1");

        controller.get(mockContext);

        verify(mockContext).status(404);
        verify(mockContext, never()).json(any());
    }

    @Test
    void testGetAll_ShouldReturnAllTeachers() {
        List<Teacher> teachers = List.of(mock(Teacher.class));
        when(mockManager.getAll()).thenReturn(teachers);
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.getAll(mockContext);

        verify(mockContext).json(teachers);
        verify(mockContext).status(200);
    }

    @Test
    void testCreate_ShouldCallAddTeacher() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("name"))).thenReturn("John Doe");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("title"))).thenReturn("Professor");

            controller.create(mockContext);

            verify(mockManager).addTeacher("John Doe", "Professor");
            verify(mockContext).status(201);
        }
    }

    @Test
    void testUpdate_ShouldCallUpdateTeacher() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("name"))).thenReturn("Jane Doe");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("title"))).thenReturn("Associate Professor");

            controller.update(mockContext);

            verify(mockManager).updateTeacher("1", "Jane Doe", "Associate Professor");
            verify(mockContext).status(204);
        }
    }

    @Test
    void testDelete_ShouldCallRemoveTeacher() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");

            controller.delete(mockContext);

            verify(mockManager).remove("1");
            verify(mockContext).status(204);
        }
    }
}