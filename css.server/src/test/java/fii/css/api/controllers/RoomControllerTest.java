package fii.css.api.controllers;

import fii.css.api.Query;
import fii.css.database.Database;
import fii.css.database.persistence.entities.Room;
import fii.css.database.persistence.entities.RoomType;
import fii.css.database.persistence.managers.RoomManager;
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

class RoomControllerTest {
    private RoomController controller;
    private RoomManager mockManager;
    private Context mockContext;

    @BeforeEach
    void setUp() {
        mockManager = mock(RoomManager.class);
        mockContext = mock(Context.class);
        Database mockDatabase = mock(Database.class);

        when(mockDatabase.getRoomManager()).thenReturn(mockManager);

        try (MockedStatic<Database> mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);
            controller = new RoomController();
        }
    }

    @Test
    void testRegisterEndpoints() {
        Javalin mockApp = mock(Javalin.class);

        controller.registerEndpoints(mockApp);

        verify(mockApp).get(eq("/rooms/{id}"), any());
        verify(mockApp).get(eq("/rooms"), any());
        verify(mockApp).post(eq("/rooms"), any());
        verify(mockApp).patch(eq("/rooms/{id}"), any());
        verify(mockApp).delete(eq("/rooms/{id}"), any());
    }


    @Test
    void testGet_ShouldReturnRoom_WhenFound() {
        Room room = new Room();
        when(mockManager.get(anyString())).thenReturn(room);
        when(mockContext.pathParam("id")).thenReturn("1");
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.get(mockContext);

        verify(mockContext).json(room);
        verify(mockContext).status(200);
    }

    @Test
    void testGet_ShouldReturn404_WhenRoomNotFound() {
        when(mockManager.get(anyString())).thenReturn(null);
        when(mockContext.pathParam("id")).thenReturn("1");

        controller.get(mockContext);

        verify(mockContext).status(404);
        verify(mockContext, never()).json(any());
    }

    @Test
    void testGetAll_ShouldReturnAllRooms() {
        List<Room> rooms = List.of(mock(Room.class));
        when(mockManager.getAll()).thenReturn(rooms);
        when(mockContext.json(any())).thenReturn(mockContext);

        controller.getAll(mockContext);

        verify(mockContext).json(rooms);
        verify(mockContext).status(200);
    }

    @Test
    void testCreate_ShouldCallAddRoom() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("name"))).thenReturn("Room 101");
            queryMock.when(() -> Query.integerParam(eq(mockContext), eq("capacity"))).thenReturn(30);
            queryMock.when(() -> Query.roomTypeParam(eq(mockContext), eq("roomType"))).thenReturn(RoomType.Laboratory);

            controller.create(mockContext);

            verify(mockManager).addRoom("Room 101", 30, RoomType.Laboratory);
            verify(mockContext).status(201);
        }
    }

    @Test
    void testUpdate_ShouldCallUpdateRoom() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");
            queryMock.when(() -> Query.stringParam(eq(mockContext), eq("name"))).thenReturn("Updated Room");
            queryMock.when(() -> Query.integerParam(eq(mockContext), eq("capacity"))).thenReturn(50);
            queryMock.when(() -> Query.roomTypeParam(eq(mockContext), eq("roomType"))).thenReturn(RoomType.Course);

            controller.update(mockContext);

            verify(mockManager).updateRoom("1", "Updated Room", 50, RoomType.Course);
            verify(mockContext).status(204);
        }
    }

    @Test
    void testDelete_ShouldCallRemoveRoom() {
        try (MockedStatic<Query> queryMock = mockStatic(Query.class)) {
            queryMock.when(() -> Query.idPathParam(mockContext)).thenReturn("1");

            controller.delete(mockContext);

            verify(mockManager).remove("1");
            verify(mockContext).status(204);
        }
    }
}