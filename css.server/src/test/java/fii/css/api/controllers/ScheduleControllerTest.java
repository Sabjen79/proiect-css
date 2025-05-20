package fii.css.api.controllers;

import fii.css.api.Query;
import fii.css.database.Database;
import fii.css.database.persistence.entities.ClassType;
import fii.css.database.persistence.entities.DayOfWeek;
import fii.css.database.persistence.entities.Schedule;
import fii.css.database.persistence.managers.ScheduleManager;
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

class ScheduleControllerTest {
    private ScheduleController controller;
    private ScheduleManager mockManager;
    private Context mockContext;

    @BeforeEach
    void setUp() {
        mockManager = mock(ScheduleManager.class);
        mockContext = mock(Context.class);
        Database mockDatabase = mock(Database.class);

        when(mockDatabase.getScheduleManager()).thenReturn(mockManager);

        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);
            controller = new ScheduleController();
        }
    }

    @Test
    void testRegisterEndpoints() {
        Javalin mockApp = mock(Javalin.class);

        controller.registerEndpoints(mockApp);

        verify(mockApp).get(eq("/schedules/{id}"), any());
        verify(mockApp).get(eq("/schedules"), any());
        verify(mockApp).post(eq("/schedules"), any());
        verify(mockApp).patch(eq("/schedules/{id}"), any());
        verify(mockApp).delete(eq("/schedules/{id}"), any());
    }


    @Test
    void testGet_ShouldReturnSchedule_WhenFound() {
        Schedule schedule = new Schedule();
        when(mockManager.get(anyString())).thenReturn(schedule);
        when(mockContext.pathParam("id")).thenReturn("1");
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.get(mockContext);

        verify(mockContext).json(schedule);
        verify(mockContext).status(200);
    }

    @Test
    void testGet_ShouldReturn404_WhenScheduleNotFound() {
        when(mockManager.get(anyString())).thenReturn(null);
        when(mockContext.pathParam("id")).thenReturn("1");

        controller.get(mockContext);

        verify(mockContext).status(404);
        verify(mockContext, never()).json(any());
    }

    @Test
    void testGetAll_ShouldReturnAllSchedules() {
        List<Schedule> schedules = List.of(mock(Schedule.class));
        when(mockManager.getAll()).thenReturn(schedules);
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.getAll(mockContext);

        verify(mockContext).json(schedules);
        verify(mockContext).status(200);
    }

    @Test
    void testCreate_ShouldCallAddSchedule() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("teacherId"))).thenReturn("T1");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("disciplineId"))).thenReturn("D1");
            queryMock.when(() -> Query.classTypeParam(eq(mockContext), eq("classType"))).thenReturn(ClassType.Laboratory);
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("roomId"))).thenReturn("R1");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("studentsId"))).thenReturn("G1");
            queryMock.when(() -> Query.dayOfWeekParam(eq(mockContext), eq("dayOfWeek"))).thenReturn(DayOfWeek.Monday);
            queryMock.when(() -> Query.integerParam(eq(mockContext), eq("startHour"))).thenReturn(8);

            controller.create(mockContext);

            verify(mockManager).addSchedule("T1", "D1", ClassType.Laboratory, "R1", "G1", DayOfWeek.Monday, 8);
            verify(mockContext).status(201);
        }
    }

    @Test
    void testUpdate_ShouldCallUpdateSchedule() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("teacherId"))).thenReturn("T2");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("disciplineId"))).thenReturn("D2");
            queryMock.when(() -> Query.classTypeParam(eq(mockContext), eq("classType"))).thenReturn(ClassType.Course);
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("roomId"))).thenReturn("R2");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("studentsId"))).thenReturn("G2");
            queryMock.when(() -> Query.dayOfWeekParam(eq(mockContext), eq("dayOfWeek"))).thenReturn(DayOfWeek.Tuesday);
            queryMock.when(() -> Query.integerParam(eq(mockContext), eq("startHour"))).thenReturn(10);

            controller.update(mockContext);

            verify(mockManager).updateSchedule("1", "T2", "D2", ClassType.Course, "R2", "G2", DayOfWeek.Tuesday, 10);
            verify(mockContext).status(204);
        }
    }

    @Test
    void testDelete_ShouldCallRemoveSchedule() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");

            controller.delete(mockContext);

            verify(mockManager).remove("1");
            verify(mockContext).status(204);
        }
    }
}
