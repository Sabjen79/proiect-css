package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.*;
import fii.css.database.persistence.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeacherDisciplineManagerTest {
    TeacherDisciplineManager teacherDisciplineManager;
    DisciplineManager disciplineManager;
    TeacherManager teacherManager;
    ScheduleManager scheduleManager;
    RoomManager roomManager;

    @BeforeEach
    void setUp() {
        FakeDisciplineRepository fakeDisciplineRepository = new FakeDisciplineRepository();
        disciplineManager = new DisciplineManager(fakeDisciplineRepository);

        FakeTeacherRepository fakeTeacherRepository = new FakeTeacherRepository();
        teacherManager = new TeacherManager(fakeTeacherRepository);

        FakeTeacherDisciplineRepository fakeRepo = new FakeTeacherDisciplineRepository();
        teacherDisciplineManager = new TeacherDisciplineManager(fakeRepo);

        FakeScheduleRepository fakeScheduleRepository = new FakeScheduleRepository();
        scheduleManager = new ScheduleManager(fakeScheduleRepository);

        FakeRoomRepository fakeRoomRepository = new FakeRoomRepository();
        roomManager = new RoomManager(fakeRoomRepository);

        // Injectează managerii în Database singleton
        var db = fii.css.database.Database.getInstance();
        db.setDisciplineManager(disciplineManager);
        db.setTeacherManager(teacherManager);
        db.setTeacherDisciplineManager(teacherDisciplineManager);
        db.setScheduleManager(scheduleManager);
        db.setRoomManager(roomManager);

    }

    @Test
    void testAddTeacherDisciplineAndRemoveDisciplineFails() {

        disciplineManager.addDiscipline("CSS", Degree.Master, 1);
        teacherManager.addTeacher("Name", "title");

        var discipline = disciplineManager.getAll().get(0);
        var teacher = teacherManager.getAll().get(0);


        teacherDisciplineManager.addTeacherDiscipline(teacher.getId(), discipline.getId());


        List<TeacherDiscipline> all = teacherDisciplineManager.getAll();
        assertEquals(1, all.size());
        assertEquals(teacher.getId(), all.get(0).getTeacherId());


        RuntimeException ex = assertThrows(RuntimeException.class, () -> disciplineManager.remove(discipline.getId()) );

        assertTrue(ex.getMessage().contains("Discipline is still associated with a teacher."));
    }

    @Test
    void testAddTeacherDisciplineAndRemoveTeacherFails() {

        disciplineManager.addDiscipline("CSS", Degree.Master, 1);
        teacherManager.addTeacher("Name", "title");

        var discipline = disciplineManager.getAll().get(0);
        var teacher = teacherManager.getAll().get(0);


        teacherDisciplineManager.addTeacherDiscipline(teacher.getId(), discipline.getId());


        List<TeacherDiscipline> all = teacherDisciplineManager.getAll();
        assertEquals(1, all.size());
        assertEquals(teacher.getId(), all.get(0).getTeacherId());


        RuntimeException ex = assertThrows(RuntimeException.class, () -> teacherManager.remove(teacher.getId()) );

        assertTrue(ex.getMessage().contains("Teacher is still associated with a discipline."));
    }

    @Test
    void testAddTeacherDisciplineFailsIfTeacherDoesNotExist() {
        disciplineManager.addDiscipline("CSS", Degree.Bachelor, 1);
        var discipline = disciplineManager.getAll().get(0);

        var ex = assertThrows(RuntimeException.class, () ->
                teacherDisciplineManager.addTeacherDiscipline("non-existent-teacher-id", discipline.getId())
        );

        assertTrue(ex.getMessage().contains("Teacher with ID"));
    }

    @Test
    void testAddTeacherDisciplineFailsIfDisciplineDoesNotExist() {
        teacherManager.addTeacher("John Doe", "Lecturer");
        var teacher = teacherManager.getAll().get(0);

        var ex = assertThrows(RuntimeException.class, () ->
                teacherDisciplineManager.addTeacherDiscipline(teacher.getId(), "non-existent-discipline-id")
        );

        assertTrue(ex.getMessage().contains("Discipline with ID"));
    }

    @Test
    void testAddTeacherDisciplineFailsIfAlreadyExists() {
        disciplineManager.addDiscipline("OOP", Degree.Bachelor, 2);
        teacherManager.addTeacher("Jane Smith", "Professor");

        var discipline = disciplineManager.getAll().get(0);
        var teacher = teacherManager.getAll().get(0);

        teacherDisciplineManager.addTeacherDiscipline(teacher.getId(), discipline.getId());

        var ex = assertThrows(RuntimeException.class, () ->
                teacherDisciplineManager.addTeacherDiscipline(teacher.getId(), discipline.getId())
        );

        assertTrue(ex.getMessage().contains("association already exists"));
    }

    @Test
    void testRemoveTeacherDisciplineSuccessfully() {
        disciplineManager.addDiscipline("AI", Degree.Master, 2);
        teacherManager.addTeacher("Alan Turing", "Researcher");

        var discipline = disciplineManager.getAll().get(0);
        var teacher = teacherManager.getAll().get(0);

        teacherDisciplineManager.addTeacherDiscipline(teacher.getId(), discipline.getId());

        var td = teacherDisciplineManager.getAll().get(0);

        // Remove the association
        teacherDisciplineManager.remove(td.getId());

        assertTrue(teacherDisciplineManager.getAll().isEmpty());
    }

    @Test
    void testRemoveTeacherDisciplineFailsIfNotExists() {
        var ex = assertThrows(RuntimeException.class, () ->
                teacherDisciplineManager.remove("non-existent-id")
        );

        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void testMultipleAssociationsAreHandledCorrectly() {
        disciplineManager.addDiscipline("Databases", Degree.Bachelor, 2);
        disciplineManager.addDiscipline("Networks", Degree.Master, 1);
        teacherManager.addTeacher("Alice", "Assistant");
        teacherManager.addTeacher("Bob", "Lecturer");

        var d1 = disciplineManager.getAll().get(0);
        var d2 = disciplineManager.getAll().get(1);
        var t1 = teacherManager.getAll().get(0);
        var t2 = teacherManager.getAll().get(1);

        teacherDisciplineManager.addTeacherDiscipline(t1.getId(), d1.getId());
        teacherDisciplineManager.addTeacherDiscipline(t2.getId(), d2.getId());
        teacherDisciplineManager.addTeacherDiscipline(t1.getId(), d2.getId());

        var all = teacherDisciplineManager.getAll();
        assertEquals(3, all.size());
    }

    @Test
    void testDuplicateTeacherDisciplineWithDifferentIdFails() {
        disciplineManager.addDiscipline("Security", Degree.Master, 2);
        teacherManager.addTeacher("Dr. Smith", "Senior Lecturer");

        var teacher = teacherManager.getAll().get(0);
        var discipline = disciplineManager.getAll().get(0);

        teacherDisciplineManager.addTeacherDiscipline(teacher.getId(), discipline.getId());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                teacherDisciplineManager.addTeacherDiscipline(teacher.getId(), discipline.getId()));
        assertTrue(ex.getMessage().contains("association already exists"));
    }

    @Test
    void testDeleteInChainAfterUnlinking() {
        disciplineManager.addDiscipline("NLP", Degree.Master, 1);
        teacherManager.addTeacher("Tina", "Lecturer");

        var teacher = teacherManager.getAll().get(0);
        var discipline = disciplineManager.getAll().get(0);

        teacherDisciplineManager.addTeacherDiscipline(teacher.getId(), discipline.getId());
        var td = teacherDisciplineManager.getAll().get(0);

        teacherDisciplineManager.remove(td.getId());

        teacherManager.remove(teacher.getId());
        disciplineManager.remove(discipline.getId());

        assertTrue(teacherManager.getAll().isEmpty());
        assertTrue(disciplineManager.getAll().isEmpty());
    }

}
