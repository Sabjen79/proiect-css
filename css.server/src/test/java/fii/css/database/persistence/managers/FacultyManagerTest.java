package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.*;
import fii.css.database.persistence.repositories.FacultyGroupRepository;
import fii.css.database.persistence.repositories.SemiYearRepository;
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

public class FacultyManagerTest {
    @Mock
    Database mockDatabase;

    @Mock
    FacultyGroupRepository mockRepository;

    @Mock
    ScheduleManager mockScheduleManager;

    @Mock
    SemiYearManager mockSemiYearManager;

    FacultyGroupManager manager;

    List<SemiYear> semiYears = List.of(
        new SemiYear("1", "A", Degree.Bachelor.value, 1),
        new SemiYear("2", "B", Degree.Master.value, 1)
    );

    List<FacultyGroup> facultyGroups = List.of(
        new FacultyGroup("11", "Group", "1"),
        new FacultyGroup("21", "Group", "2")
    );

    List<Schedule> schedules = List.of(
        new Schedule("", "", "", 0, "", "1", 0, 0),
        new Schedule("", "", "", 1, "", "21", 0, 0)
    );

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockRepository = mock(FacultyGroupRepository.class);
        mockScheduleManager = mock(ScheduleManager.class);
        mockSemiYearManager = mock(SemiYearManager.class);
        manager = new FacultyGroupManager(mockRepository);

        when(mockDatabase.getFacultyGroupManager()).thenReturn(manager);
        when(mockDatabase.getScheduleManager()).thenReturn(mockScheduleManager);
        when(mockDatabase.getSemiYearManager()).thenReturn(mockSemiYearManager);

        when(mockRepository.getAll()).thenAnswer((a) -> facultyGroups);
        when(mockScheduleManager.getAll()).thenAnswer((a) -> schedules);
        when(mockSemiYearManager.getAll()).thenAnswer((a) -> semiYears);

        when(mockSemiYearManager.get("1")).thenAnswer(a -> semiYears.get(0).clone());
        when(mockSemiYearManager.get("2")).thenAnswer(a -> semiYears.get(1).clone());

        when(mockRepository.getById("11")).thenAnswer(a -> facultyGroups.get(0).clone());
        when(mockRepository.getById("21")).thenAnswer(a -> facultyGroups.get(1).clone());
    }

    @Test
    public void testAdd() {
        try(var mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            when(mockRepository.newEntity()).thenReturn(new FacultyGroup("999", "", ""));

            assertDoesNotThrow(() -> manager.addFacultyGroup("Grupa", "2"));

            assertThrows(DatabaseException.class, () -> manager.addFacultyGroup("Hello", "100"));
            assertThrows(DatabaseException.class, () -> manager.addFacultyGroup("", "1"));
            assertThrows(DatabaseException.class, () -> manager.addFacultyGroup("Group", "2"));

            verify(mockRepository, times(1)).persist(any(FacultyGroup.class));
        }
    }

    @Test
    public void testUpdate() {
        try(var mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            when(mockRepository.getById("1")).thenReturn(facultyGroups.get(0));

            assertDoesNotThrow(() -> manager.updateFacultyGroup("11", "Group", "1"));

            assertThrows(DatabaseException.class, () -> manager.updateFacultyGroup("100", "Group", "1"));
            assertThrows(DatabaseException.class, () -> manager.updateFacultyGroup("100", "Group", "1"));
            assertThrows(DatabaseException.class, () -> manager.updateFacultyGroup("21", "Group1", "1"));
            assertThrows(DatabaseException.class, () -> manager.updateFacultyGroup("11", "Group", "2"));

            verify(mockRepository, times(1)).merge(any(FacultyGroup.class));
        }
    }

    @Test
    public void testRemove() {
        try(var mockedStatic = mockStatic(Database.class)) {
            mockedStatic.when(Database::getInstance).thenReturn(mockDatabase);

            when(mockRepository.getById("1")).thenReturn(facultyGroups.get(0));

            assertDoesNotThrow(() -> manager.remove("11"));

            assertThrows(DatabaseException.class, () -> manager.remove("21"));
            assertThrows(DatabaseException.class, () -> manager.remove("100"));

            verify(mockRepository, times(1)).delete(any(FacultyGroup.class));
        }
    }
}
