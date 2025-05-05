package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.SemiYear;
import fii.css.database.persistence.entities.TeacherDiscipline;
import fii.css.database.persistence.repositories.SemiYearRepository;
import fii.css.database.persistence.repositories.TeacherDisciplineRepository;

import java.util.List;

public class TeacherDisciplineManager  extends AbstractEntityManager<TeacherDiscipline> {
    public TeacherDisciplineManager() {
        super(new TeacherDisciplineRepository());
    }

    @Override
    public TeacherDiscipline get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<TeacherDiscipline> getAll() {
        return repository.getAll();
    }

    public void addTeacherDiscipline(String teacher_id, String discipline_id) {
        TeacherDiscipline entity = repository.newEntity();

        entity.setTeacherId(teacher_id);
        entity.setDisciplineId(discipline_id);

        validate(entity);

        repository.persist(entity);
    }

    @Override
    public void remove(String id) {
        TeacherDiscipline td = repository.getById(id);

        if (td == null) {
            throw new DatabaseException("TeacherDiscipline with ID '" + id + "' does not exist.");
        }

        var sManager = Database.getInstance().scheduleManager;

        // Because disciplines and teachers are always correlated in schedule,
        // we can only check for one of them
        for(var s : sManager.getAll()) {
            if(s.getTeacher().getId().equals(td.getTeacherId())) {
                throw new DatabaseException("Teacher-Discipline relation is still being referenced in schedule.");
            }
        }

        repository.delete(td);
    }

    private void validate(TeacherDiscipline teacherDiscipline) {
        if(Database.getInstance().teacherManager.get(teacherDiscipline.getTeacherId()) == null) {
            throw new DatabaseException("Teacher with ID '" + teacherDiscipline.getTeacherId() + "' does not exist.");
        }

        if(Database.getInstance().disciplineManager.get(teacherDiscipline.getDisciplineId()) == null) {
            throw new DatabaseException("Discipline with ID '" + teacherDiscipline.getDisciplineId() + "' does not exist.");
        }

        for(var td : getAll()) {
            if(!td.getId().equals(teacherDiscipline.getId())
            && td.getDisciplineId().equals(teacherDiscipline.getDisciplineId())
            && td.getTeacherId().equals(teacherDiscipline.getTeacherId())) {
                throw new DatabaseException("This association already exists.");
            }
        }
    }
}
