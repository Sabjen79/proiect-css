package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.*;
import fii.css.database.persistence.repositories.FacultyGroupRepository;
import fii.css.database.persistence.repositories.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ScheduleManagerTest {
    @Mock
    Database mockDatabase;

    @Mock
    ScheduleRepository mockRepository;

    @Mock
    DisciplineManager mockDisciplineManager;

    @Mock
    TeacherManager mockTeacherManager;

    @Mock
    TeacherDisciplineManager mockTeacherDisciplineManager;

    @Mock
    SemiYearManager mockSemiYearManager;

    @Mock
    FacultyGroupManager mockFacultyGroupManager;

    @Mock
    RoomManager mockRoomManager;

    ScheduleManager manager;

    List<Teacher> teachers = List.of(
        new Teacher("t1", "Name1", "Dr"),
        new Teacher("t2", "Name2", "Dr"),
        new Teacher("t3", "Name3", "Dr")
    );

    List<Discipline> disciplines = List.of(
        new Discipline("d1", "D1", Degree.Bachelor.value, 1),
        new Discipline("d2", "D2", Degree.Bachelor.value, 2),
        new Discipline("d3", "D3", Degree.Master.value, 1),
        new Discipline("d4", "D4", Degree.Bachelor.value, 1)
    );

    List<TeacherDiscipline> teacherDisciplines = List.of(
        new TeacherDiscipline("td1", "t1", "d1"),
        new TeacherDiscipline("td2", "t2", "d2"),
        new TeacherDiscipline("td3", "t3", "d4")
    );

    List<Room> rooms = List.of(
        new Room("r1", "R1", 100, RoomType.Course.value),
        new Room("r2", "R2", 100, RoomType.Laboratory.value),
        new Room("r3", "R3", 100, RoomType.Seminary.value)
    );

    List<SemiYear> semiYears = List.of(
        new SemiYear("s1", "A", Degree.Bachelor.value, 2),
        new SemiYear("s2", "B", Degree.Bachelor.value, 1)
    );

    List<FacultyGroup> facultyGroups = List.of(
        new FacultyGroup("f1", "Group", "s1"),
        new FacultyGroup("f2", "Group1", "s2"),
        new FacultyGroup("f3", "Group2", "s2")
    );

    List<Schedule> schedules = List.of(
        new Schedule("1", "t1", "d1", ClassType.Course.value, "r1", "s2", DayOfWeek.Monday.value, 8),
        new Schedule("1", "t1", "d1", ClassType.Laboratory.value, "r2", "f2", DayOfWeek.Monday.value, 10)
    );

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockRepository = mock(ScheduleRepository.class);
        mockDisciplineManager = mock(DisciplineManager.class);
        mockTeacherDisciplineManager = mock(TeacherDisciplineManager.class);
        mockTeacherManager = mock(TeacherManager.class);
        mockSemiYearManager = mock(SemiYearManager.class);
        mockFacultyGroupManager = mock(FacultyGroupManager.class);
        mockRoomManager = mock(RoomManager.class);

        manager = new ScheduleManager(mockRepository);

        when(mockDatabase.getTeacherManager()).thenReturn(mockTeacherManager);
        when(mockDatabase.getTeacherDisciplineManager()).thenReturn(mockTeacherDisciplineManager);
        when(mockDatabase.getDisciplineManager()).thenReturn(mockDisciplineManager);
        when(mockDatabase.getSemiYearManager()).thenReturn(mockSemiYearManager);
        when(mockDatabase.getFacultyGroupManager()).thenReturn(mockFacultyGroupManager);
        when(mockDatabase.getRoomManager()).thenReturn(mockRoomManager);
        when(mockDatabase.getScheduleManager()).thenReturn(manager);

        when(mockTeacherManager.getAll()).thenAnswer((a) -> teachers);
        when(mockDisciplineManager.getAll()).thenAnswer((a) -> disciplines);
        when(mockTeacherDisciplineManager.getAll()).thenAnswer((a) -> teacherDisciplines);
        when(mockSemiYearManager.getAll()).thenAnswer((a) -> semiYears);
        when(mockFacultyGroupManager.getAll()).thenAnswer((a) -> facultyGroups);
        when(mockRoomManager.getAll()).thenAnswer((a) -> rooms);
        when(mockRepository.getAll()).thenAnswer((a) -> schedules);

        for (Teacher teacher : teachers) {
            when(mockTeacherManager.get(teacher.getId())).thenAnswer(a -> teacher.clone());
        }

        for (Discipline discipline : disciplines) {
            when(mockDisciplineManager.get(discipline.getId())).thenAnswer(a -> discipline.clone());
        }

        for (TeacherDiscipline teacherDiscipline : teacherDisciplines) {
            when(mockTeacherDisciplineManager.get(teacherDiscipline.getId())).thenAnswer(a -> teacherDiscipline.clone());
        }

        for (Room room : rooms) {
            when(mockRoomManager.get(room.getId())).thenAnswer(a -> room.clone());
        }

        for (SemiYear semiYear : semiYears) {
            when(mockSemiYearManager.get(semiYear.getId())).thenAnswer(a -> semiYear.clone());
        }

        for (FacultyGroup facultyGroup : facultyGroups) {
            when(mockFacultyGroupManager.get(facultyGroup.getId())).thenAnswer(a -> facultyGroup.clone());
        }

        for (Schedule schedule : schedules) {
            when(mockRepository.getById(schedule.getId())).thenAnswer(a -> schedule.clone());
        }
    }

    @Test
    public void testAdd() {
        try(var mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            when(mockRepository.newEntity()).thenReturn(
                new Schedule("ID", "", "", 0, "", "", 0, 0)
            );

            assertDoesNotThrow(() -> manager.addSchedule("t2", "d2", ClassType.Laboratory, "r2", "f1", DayOfWeek.Monday, 18));

            // Null/Empty Check
            assertThrows(AssertionError.class, () -> manager.addSchedule("", "d1", ClassType.Laboratory, "r2", "f1", DayOfWeek.Monday, 18));
            assertThrows(AssertionError.class, () -> manager.addSchedule("t1", "", ClassType.Laboratory, "r2", "f1", DayOfWeek.Monday, 18));
            assertThrows(AssertionError.class, () -> manager.addSchedule("t1", "d1", ClassType.Laboratory, "", "f1", DayOfWeek.Monday, 18));
            assertThrows(AssertionError.class, () -> manager.addSchedule("t1", "d1", ClassType.Laboratory, "r2", "", DayOfWeek.Monday, 18));

            // Hour
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d1", ClassType.Course, "r1", "s1", DayOfWeek.Monday, 6));
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d1", ClassType.Course, "r1", "s1", DayOfWeek.Monday, 22));
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d1", ClassType.Course, "r1", "s1", DayOfWeek.Monday, 11));

            // ClassType-Room
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d1", ClassType.Laboratory, "r1", "f1", DayOfWeek.Monday, 8));
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d1", ClassType.Course, "r2", "s1", DayOfWeek.Monday, 8));

            // Discipline-Group
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d2", ClassType.Laboratory, "r2", "f1", DayOfWeek.Monday, 18));
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d3", ClassType.Laboratory, "r2", "f1", DayOfWeek.Monday, 18));

            // Discipline-Teacher
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t2", "d1", ClassType.Laboratory, "r2", "f1", DayOfWeek.Monday, 18));

            // Week Restrictions
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d1", ClassType.Course, "r1", "s2", DayOfWeek.Monday, 18));
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d1", ClassType.Laboratory, "r2", "f2", DayOfWeek.Monday, 10));

            // Overlap
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d1", ClassType.Laboratory, "r2", "f3", DayOfWeek.Monday, 8));
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t2", "d2", ClassType.Laboratory, "r2", "f1", DayOfWeek.Monday, 10));
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t3", "d4", ClassType.Seminary, "r3", "f2", DayOfWeek.Monday, 10));
            assertThrows(DatabaseException.class, () -> manager.addSchedule("t3", "d4", ClassType.Seminary, "r3", "f2", DayOfWeek.Monday, 8));


            //assertThrows(DatabaseException.class, () -> manager.addSchedule("t1", "d1", ClassType.Laboratory, "r2", "f1", DayOfWeek.Monday, 18));
            verify(mockRepository, times(1)).persist(any(Schedule.class));
        }
    }

    @Test
    public void testUpdate() {
        try(var mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            assertDoesNotThrow(() -> manager.updateSchedule("1", "t2", "d2", ClassType.Laboratory, "r2", "f1", DayOfWeek.Monday, 18));

            assertThrows(DatabaseException.class, () -> manager.updateSchedule("??", "t1", "d1", ClassType.Course, "r1", "s1", DayOfWeek.Friday, 8));

            verify(mockRepository, times(1)).merge(any(Schedule.class));
        }
    }

    @Test
    public void testRemove() {
        try(var mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            assertDoesNotThrow(() -> manager.remove("1"));

            verify(mockRepository, times(1)).delete(any(Schedule.class));
        }
    }
}
