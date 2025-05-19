
package fii.css.database.persistence.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisciplineTest {

    @Test
    void testGettersAndSetters() {
        Discipline discipline = new Discipline();
        discipline.setName("CSS");
        discipline.setYear(2);
        discipline.setDegree(Degree.Master);

        assertEquals("CSS", discipline.getName());
        assertEquals(2, discipline.getYear());
        assertEquals(Degree.Master, discipline.getDegree());
    }

    @Test
    void testCloneMethod() {
        Discipline original = new Discipline();
        original.setName("IP");
        original.setYear(1);
        original.setDegree(Degree.Bachelor);

        Discipline copy = (Discipline) original.clone();

        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getYear(), copy.getYear());
        assertEquals(original.getDegree(), copy.getDegree());
    }
}