package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.managers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScheduleTest {

    Schedule schedule;

    @BeforeEach
    void setUp() {
        schedule = new Schedule();
        schedule.setTeacherId("T1");
        schedule.setDisciplineId("D1");
        schedule.setRoomId("R1");
        schedule.setStudentsId("G1");
        schedule.setClassType(ClassType.Laboratory);
        schedule.setDayOfWeek(DayOfWeek.Tuesday);
        schedule.setStartHour(8);
    }

    @Test
    void testBasicFieldsAndEnums() {
        assertEquals("T1", schedule.getTeacherId());
        assertEquals("D1", schedule.getDisciplineId());
        assertEquals("R1", schedule.getRoomId());
        assertEquals("G1", schedule.getStudentsId());
        assertEquals(ClassType.Laboratory, schedule.getClassType());
        assertEquals(DayOfWeek.Tuesday, schedule.getDayOfWeek());
        assertEquals(8, schedule.getStartHour());
    }

    @Test
    void testCloneCreatesCopy() throws Exception {
        // Set ID via reflection
        Field idField = Schedule.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(schedule, "SCHED123");

        Schedule copy = (Schedule) schedule.clone();

        assertNotSame(schedule, copy);
        assertEquals("SCHED123", copy.getId());
        assertEquals(schedule.getTeacherId(), copy.getTeacherId());
        assertEquals(schedule.getDisciplineId(), copy.getDisciplineId());
        assertEquals(schedule.getRoomId(), copy.getRoomId());
        assertEquals(schedule.getStudentsId(), copy.getStudentsId());
        assertEquals(schedule.getClassType(), copy.getClassType());
        assertEquals(schedule.getDayOfWeek(), copy.getDayOfWeek());
        assertEquals(schedule.getStartHour(), copy.getStartHour());
    }

    @Test
    void testGetTeacher() throws Exception {
        Schedule schedule = new Schedule();
        schedule.setTeacherId("T1");
        Teacher teacher = new Teacher();
        Field idField = Teacher.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(teacher, "T1");

        // Mock the TeacherManager
        TeacherManager mockTeacherManager = mock(TeacherManager.class);
        when(mockTeacherManager.get("T1")).thenReturn(teacher);

        // Mock Database.getInstance() to return a mock with our manager stubbed
        Database mockDb = mock(Database.class);
        when(mockDb.getTeacherManager()).thenReturn(mockTeacherManager);

        try (MockedStatic<Database> dbMock = mockStatic(Database.class)) {
            dbMock.when(Database::getInstance).thenReturn(mockDb);

            Teacher result = schedule.getTeacher();

            assertNotNull(result);
            assertEquals("T1", result.getId());
            assertEquals(teacher, result);
        }
    }


    @Test
    void testGetRoom() {
        Room room = new Room();
        room.setName("Room 101");

        RoomManager rm = mock(RoomManager.class);
        when(rm.get("R1")).thenReturn(room);

        Database db = mock(Database.class);
        when(db.getRoomManager()).thenReturn(rm);

        try (MockedStatic<Database> mockDb = mockStatic(Database.class)) {
            mockDb.when(Database::getInstance).thenReturn(db);

            schedule.setRoomId("R1");
            assertEquals(room, schedule.getRoom());
        }
    }

    @Test
    void testGetGroupForCourse() {
        schedule.setClassType(ClassType.Course);

        SemiYear semiYear = new SemiYear();
        semiYear.setName("Year 1");

        SemiYearManager sm = mock(SemiYearManager.class);
        when(sm.get("G1")).thenReturn(semiYear);

        Database db = mock(Database.class);
        when(db.getSemiYearManager()).thenReturn(sm);

        try (MockedStatic<Database> mockDb = mockStatic(Database.class)) {
            mockDb.when(Database::getInstance).thenReturn(db);

            schedule.setStudentsId("G1");
            assertEquals(semiYear, schedule.getGroup());
        }
    }

    @Test
    void testGetGroupForLabOrSeminary() {
        schedule.setClassType(ClassType.Laboratory);

        FacultyGroup group = new FacultyGroup();
        group.setName("Group A");

        FacultyGroupManager fm = mock(FacultyGroupManager.class);
        when(fm.get("G1")).thenReturn(group);

        Database db = mock(Database.class);
        when(db.getFacultyGroupManager()).thenReturn(fm);

        try (MockedStatic<Database> mockDb = mockStatic(Database.class)) {
            mockDb.when(Database::getInstance).thenReturn(db);

            schedule.setStudentsId("G1");
            assertEquals(group, schedule.getGroup());
        }
    }

    @Test
    void testGetSemiYearForCourse() throws NoSuchFieldException, IllegalAccessException {
        schedule.setClassType(ClassType.Course);

        SemiYear semiYear = new SemiYear();
        Field id = SemiYear.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(semiYear, "SY1");

        SemiYearManager sm = mock(SemiYearManager.class);
        when(sm.get("G1")).thenReturn(semiYear);

        Database db = mock(Database.class);
        when(db.getSemiYearManager()).thenReturn(sm);

        try (MockedStatic<Database> mockDb = mockStatic(Database.class)) {
            mockDb.when(Database::getInstance).thenReturn(db);

            schedule.setStudentsId("G1");
            assertEquals(semiYear, schedule.getSemiYear());
        }
    }

    @Test
    void testGetSemiYearForSeminary() throws Exception {
        schedule.setClassType(ClassType.Seminary);

        FacultyGroup group = new FacultyGroup();
        group.setSemiYearId("SY2");

        SemiYear semiYear = new SemiYear();
        Field idField = SemiYear.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(semiYear, "SY2");

        FacultyGroupManager fgManager = mock(FacultyGroupManager.class);
        when(fgManager.get("G1")).thenReturn(group);

        SemiYearManager sm = mock(SemiYearManager.class);
        when(sm.get("SY2")).thenReturn(semiYear);

        Database db = mock(Database.class);
        when(db.getFacultyGroupManager()).thenReturn(fgManager);
        when(db.getSemiYearManager()).thenReturn(sm);

        try (MockedStatic<Database> mockDb = mockStatic(Database.class)) {
            mockDb.when(Database::getInstance).thenReturn(db);

            schedule.setStudentsId("G1");
            assertEquals(semiYear, schedule.getSemiYear());
        }
    }

}

