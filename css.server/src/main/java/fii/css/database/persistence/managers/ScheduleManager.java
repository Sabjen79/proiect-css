package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.*;
import fii.css.database.persistence.repositories.ScheduleRepository;

import java.util.List;

public class ScheduleManager extends AbstractEntityManager<Schedule> {
    public ScheduleManager() {
        super(new ScheduleRepository());
    }

    @Override
    public Schedule get(String id) {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Schedule> getAll() {
        // TODO: Implement this
        throw new UnsupportedOperationException();
    }

    public Schedule addSchedule(
        TeacherDiscipline teacherDiscipline,
        ClassType classType,
        Room room,
        FacultyGroup facultyGroup,
        DayOfWeek dayOfWeek,
        int startHour,
        int endHour
    ) {
        // TODO: Implement this :(
        throw new UnsupportedOperationException();
    }

    public Schedule updateSchedule(
            String id,
            TeacherDiscipline teacherDiscipline,
            ClassType classType,
            Room room,
            FacultyGroup facultyGroup,
            DayOfWeek dayOfWeek,
            int startHour,
            int endHour
    ) {
        // TODO: Implement this :(
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(String id) {
        repository.delete(repository.getById(id));
    }
}
