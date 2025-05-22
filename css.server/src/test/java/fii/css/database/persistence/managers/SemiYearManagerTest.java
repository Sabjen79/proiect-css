package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.*;
import fii.css.database.persistence.repositories.ScheduleRepository;
import fii.css.database.persistence.repositories.SemiYearRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SemiYearManagerTest {
    @Mock
    Database mockDatabase;

    @Mock
    SemiYearRepository mockRepository;

    @Mock
    ScheduleManager mockScheduleManager;

    @Mock
    FacultyGroupManager mockFacultyGroupManager;

    SemiYearManager manager;

    List<SemiYear> semiYears = List.of(
        new SemiYear("1", "SemiYear", Degree.Bachelor.value, 1),
        new SemiYear("2", "SemiYear", Degree.Master.value, 2),
        new SemiYear("3", "A", Degree.Master.value, 1)
    );

    List<FacultyGroup> facultyGroups = List.of(
        new FacultyGroup("1", "Group1", "1")
    );

    List<Schedule> schedules = List.of(
        new Schedule("1", "", "", ClassType.Course.value, "", "3", 0, 0)
    );

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockRepository = mock(SemiYearRepository.class);
        mockScheduleManager = mock(ScheduleManager.class);
        mockFacultyGroupManager = mock(FacultyGroupManager.class);
        manager = new SemiYearManager(mockRepository);

        when(mockDatabase.getSemiYearManager()).thenReturn(manager);
        when(mockDatabase.getScheduleManager()).thenReturn(mockScheduleManager);
        when(mockDatabase.getFacultyGroupManager()).thenReturn(mockFacultyGroupManager);

        when(mockFacultyGroupManager.getAll()).thenAnswer((a) -> facultyGroups);
        when(mockScheduleManager.getAll()).thenAnswer((a) -> schedules);
        when(mockRepository.getAll()).thenAnswer((a) -> semiYears);

        when(mockRepository.getById("1")).thenAnswer((a) -> semiYears.get(0).clone());
        when(mockRepository.getById("2")).thenAnswer((a) -> semiYears.get(1).clone());
        when(mockRepository.getById("3")).thenAnswer((a) -> semiYears.get(2).clone());
    }

    @Test
    public void testAdd() {
        try(var mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            when(mockRepository.newEntity()).thenReturn(new SemiYear("999", "", 0, 0));

            assertDoesNotThrow(() -> manager.addStudyYear("test1", Degree.Bachelor, 1));
            assertDoesNotThrow(() -> manager.addStudyYear("test2", Degree.Master, 2));

            assertThrows(DatabaseException.class, () -> manager.addStudyYear("test", Degree.Bachelor, 5));
            assertThrows(AssertionError.class, () -> manager.addStudyYear("", Degree.Bachelor, 3));
            assertThrows(DatabaseException.class, () -> manager.addStudyYear("test", Degree.Master, 5));
            assertThrows(DatabaseException.class, () -> manager.addStudyYear("test", Degree.Master, -2));
            assertThrows(DatabaseException.class, () -> manager.addStudyYear("SemiYear", Degree.Bachelor, 1));

            verify(mockRepository, times(2)).persist(any(SemiYear.class));
        }
    }

    @Test
    public void testUpdate() {
        try(var mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            assertDoesNotThrow(() -> manager.updateStudyYear("2", "test1", Degree.Bachelor, 1));
            assertDoesNotThrow(() -> manager.updateStudyYear("3", "AB", Degree.Master, 1));

            assertThrows(DatabaseException.class, () -> manager.updateStudyYear("100", "SemiYear", Degree.Bachelor, 1));
            assertThrows(DatabaseException.class, () -> manager.updateStudyYear("2", "SemiYear", Degree.Bachelor, 1));
            assertThrows(DatabaseException.class, () -> manager.updateStudyYear("2", "SemiYear", Degree.Master, 3));
            assertThrows(DatabaseException.class, () -> manager.updateStudyYear("3", "A", Degree.Master, 2));
            assertThrows(DatabaseException.class, () -> manager.updateStudyYear("3", "A", Degree.Bachelor, 1));

            verify(mockRepository, times(2)).merge(any(SemiYear.class));
        }
    }

    @Test
    public void testRemove() {
        try(var mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            assertDoesNotThrow(() -> manager.remove("2"));

            assertThrows(DatabaseException.class, () -> manager.remove("100"));
            assertThrows(DatabaseException.class, () -> manager.remove("1"));
            assertThrows(DatabaseException.class, () -> manager.remove("3"));

            verify(mockRepository, times(1)).delete(any(SemiYear.class));
        }
    }
}