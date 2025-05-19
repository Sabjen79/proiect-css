package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.ClassType;
import fii.css.database.persistence.entities.DayOfWeek;
import fii.css.database.persistence.entities.Schedule;
import fii.css.database.persistence.entities.TeacherDiscipline;
import fii.css.database.persistence.repositories.FakeScheduleRepository;
import fii.css.database.persistence.repositories.FakeTeacherRepository;
import fii.css.database.persistence.repositories.ScheduleRepository;
import fii.css.database.persistence.repositories.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;



import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeacherManagerTest {
    TeacherDisciplineManager teacherDisciplineManager;
    DisciplineManager disciplineManager;
    TeacherManager manager;
    ScheduleManager scheduleManager;

    @BeforeEach
    void setUp() {
        FakeTeacherRepository fakeRepo = new FakeTeacherRepository();
        manager = new TeacherManager(fakeRepo);

        FakeScheduleRepository fakeScheduleRepo = new FakeScheduleRepository();
        scheduleManager = new ScheduleManager(fakeScheduleRepo);


    }


    @Test
    void testAddTeacherNameMustBeUnique() {
        manager.addTeacher("Name", "title");
        var all = manager.getAll();
        assertEquals(1, all.size());
        assertEquals("Name", all.get(0).getName());
    }

    @Test
    void testAddTeacherFailsIfNameIsEmpty() {
        var exception = assertThrows(DatabaseException.class, () ->
                manager.addTeacher(" ", "Professor")
        );
        assertEquals("Teacher name cannot be empty.", exception.getMessage());
    }

    @Test
    void testAddTeacherFailsIfTitleIsEmpty() {
        var exception = assertThrows(DatabaseException.class, () ->
                manager.addTeacher("John Doe", "")
        );
        assertEquals("Teacher title cannot be empty.", exception.getMessage());
    }

    @Test
    void testAddTeacherFailsOnDuplicateName() {
        manager.addTeacher("John", "Professor");

        var exception = assertThrows(DatabaseException.class, () ->
                manager.addTeacher("John", "Assistant")
        );
        assertEquals("Teacher with name 'John' already exists.", exception.getMessage());
    }

    @Test
    void testUpdateTeacherThrowsIfNotExists() {
        var exception = assertThrows(NullPointerException.class, () ->
                manager.updateTeacher("nonexistent-id", "New Name", "New Title")
        );
        assertTrue(exception.getMessage().contains("is null"));
    }

    @Test
    void testRemoveFailsIfTeacherDoesNotExist() {
        var exception = assertThrows(DatabaseException.class, () ->
                manager.remove("unknown-id")
        );
        assertEquals("Teacher with ID unknown-id does not exist.", exception.getMessage());
    }



}
