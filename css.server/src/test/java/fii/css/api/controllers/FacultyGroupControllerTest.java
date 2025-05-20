package fii.css.api.controllers;

import fii.css.api.Query;
import fii.css.database.Database;
import fii.css.database.persistence.entities.FacultyGroup;
import fii.css.database.persistence.managers.FacultyGroupManager;
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

class FacultyGroupControllerTest {
    private FacultyGroupController controller;
    private FacultyGroupManager mockManager;
    private Context mockContext;

    @BeforeEach
    void setUp() {
        mockManager = mock(FacultyGroupManager.class);
        mockContext = mock(Context.class);
        Database mockDatabase = mock(Database.class);

        when(mockDatabase.getFacultyGroupManager()).thenReturn(mockManager);

        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);
            controller = new FacultyGroupController();
        }
    }

    @Test
    void testRegisterEndpoints() {
        Javalin mockApp = mock(Javalin.class);

        controller.registerEndpoints(mockApp);

        verify(mockApp).get(eq("/facultyGroups/{id}"), any());
        verify(mockApp).get(eq("/facultyGroups"), any());
        verify(mockApp).post(eq("/facultyGroups"), any());
        verify(mockApp).patch(eq("/facultyGroups/{id}"), any());
        verify(mockApp).delete(eq("/facultyGroups/{id}"), any());
    }


    @Test
    void testGet_ShouldReturnFacultyGroup_WhenFound() {
        FacultyGroup group = new FacultyGroup();
        when(mockManager.get(anyString())).thenReturn(group);
        when(mockContext.pathParam("id")).thenReturn("1");
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.get(mockContext);

        verify(mockContext).json(group);
        verify(mockContext).status(200);
    }

    @Test
    void testGet_ShouldReturn404_WhenFacultyGroupNotFound() {
        when(mockManager.get(anyString())).thenReturn(null);
        when(mockContext.pathParam("id")).thenReturn("1");

        controller.get(mockContext);

        verify(mockContext).status(404);
        verify(mockContext, never()).json(any());
    }

    @Test
    void testGetAll_ShouldReturnAllFacultyGroups() {
        List<FacultyGroup> groups = List.of(mock(FacultyGroup.class));
        when(mockManager.getAll()).thenReturn(groups);
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.getAll(mockContext);

        verify(mockContext).json(groups);
        verify(mockContext).status(200);
    }

    @Test
    void testCreate_ShouldCallAddFacultyGroup() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("name"))).thenReturn("Group A");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("semiYearId"))).thenReturn("SY1");

            controller.create(mockContext);

            verify(mockManager).addFacultyGroup("Group A", "SY1");
            verify(mockContext).status(201);
        }
    }

    @Test
    void testUpdate_ShouldCallUpdateFacultyGroup() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("name"))).thenReturn("Group B");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("semiYearId"))).thenReturn("SY2");

            controller.update(mockContext);

            verify(mockManager).updateFacultyGroup("1", "Group B", "SY2");
            verify(mockContext).status(204);
        }
    }

    @Test
    void testDelete_ShouldCallRemoveFacultyGroup() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");

            controller.delete(mockContext);

            verify(mockManager).remove("1");
            verify(mockContext).status(204);
        }
    }
}