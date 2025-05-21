package fii.css.database.persistence.managers;

import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.Discipline;
import fii.css.database.persistence.repositories.FakeDisciplineRepository;
import fii.css.database.persistence.repositories.FakeScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DisciplineManagerTest {
    DisciplineManager disciplineManager;
    ScheduleManager scheduleManager;

    @BeforeEach
    void setUp() {
        FakeDisciplineRepository fakeRepo = new FakeDisciplineRepository();
        disciplineManager = new DisciplineManager(fakeRepo);

        FakeScheduleRepository fakeScheduleRepo = new FakeScheduleRepository();
        scheduleManager = new ScheduleManager(fakeScheduleRepo);

        var db = fii.css.database.Database.getInstance();
        db.setDisciplineManager(disciplineManager);
        db.setScheduleManager(scheduleManager);
    }

    @Test
    void testAddDisciplineValid() {
        disciplineManager.addDiscipline("CSS", Degree.Master, 1);
        var all = disciplineManager.getAll();
        assertEquals(1, all.size());
        assertEquals("CSS", all.get(0).getName());
    }

    @Test
    void testDuplicateNameFails() {
        disciplineManager.addDiscipline("OOP", Degree.Bachelor, 2);
        DatabaseException ex = assertThrows(DatabaseException.class, () ->
                disciplineManager.addDiscipline("OOP", Degree.Bachelor, 3)
        );

        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    void testAddDisciplineInvalidYear() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                disciplineManager.addDiscipline("CSS", Degree.Master, 5));
        assertTrue(ex.getMessage().contains("Year must be less or equal"));
    }

    @Test
    void testGetDisciplineById() {
        disciplineManager.addDiscipline("ML", Degree.Master, 1);
        Discipline d = disciplineManager.getAll().get(0);
        Discipline found = disciplineManager.get(d.getId());
        assertNotNull(found);
        assertEquals(d.getName(), found.getName());
    }

    @Test
    void testUpdateDiscipline() {
        disciplineManager.addDiscipline("ML", Degree.Master, 1);
        Discipline d = disciplineManager.getAll().get(0);

        disciplineManager.updateDiscipline(d.getId(), "ML Updated", Degree.Master, 1);
        Discipline updated = disciplineManager.get(d.getId());

        assertEquals("ML Updated", updated.getName());
        assertEquals(Degree.Master, updated.getDegree());
        assertEquals(1, updated.getYear());
    }



    @Test
    void testRemoveNonExistentDiscipline() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> disciplineManager.remove("nonexistent-id"));
        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void testUpdateNonExistentDiscipline() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                disciplineManager.updateDiscipline("nonexistent-id", "Nope", Degree.Master, 1));

        assertTrue(ex.getMessage().contains("does not exist"));
    }
}
