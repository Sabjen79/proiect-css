package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.DatabaseException;
import fii.css.database.persistence.entities.*;
import fii.css.database.persistence.repositories.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ScheduleManager extends AbstractEntityManager<Schedule> {
    public ScheduleManager() {
        super(new ScheduleRepository());
    }

    @Override
    public Schedule get(String id) {
        return repository.getById(id);
    }

    @Override
    public List<Schedule> getAll() {
        return repository.getAll();
    }

    public void addSchedule(String teacherId, String disciplineId, ClassType classType, String roomId, String studentsId, DayOfWeek dayOfWeek, int startHour) {
        var entity = repository.newEntity();

        entity.setTeacherId(teacherId);
        entity.setDisciplineId(disciplineId);
        entity.setClassType(classType);
        entity.setRoomId(roomId);
        entity.setStudentsId(studentsId);
        entity.setDayOfWeek(dayOfWeek);
        entity.setStartHour(startHour);

        validate(entity);

        repository.persist(entity);
    }

    public void updateSchedule(String id, String teacherId, String disciplineId, ClassType classType, String roomId, String studentsId, DayOfWeek dayOfWeek, int startHour) {
        var entity = repository.getById(id);

        if (entity == null) {
            throw new RuntimeException("Schedule with ID " + id + " does not exist.");
        }

        entity.setTeacherId(teacherId);
        entity.setDisciplineId(disciplineId);
        entity.setClassType(classType);
        entity.setRoomId(roomId);
        entity.setStudentsId(studentsId);
        entity.setDayOfWeek(dayOfWeek);
        entity.setStartHour(startHour);

        validate(entity);

        repository.merge(entity);
    }

    @Override
    public void remove(String id) {
        repository.delete(repository.getById(id));
    }

    private void validate(Schedule schedule) {
        // Null Check
        if(schedule.getTeacher() == null) {
            throw new DatabaseException("Teacher not found");
        }

        if(schedule.getDiscipline() == null) {
            throw new DatabaseException("Discipline not found");
        }

        if(schedule.getRoom() == null) {
            throw new DatabaseException("Room not found");
        }

        if(schedule.getGroup() == null) {
            throw new DatabaseException("Group/Semi-year not found. Courses can only reference semi-years and labs/seminaries can only reference faculty groups");
        }

        // Hour
        if(schedule.getStartHour() < 8 || schedule.getStartHour() > 20) {
            throw new DatabaseException("Hour must be between 8 and 20");
        }

        if(schedule.getStartHour() % 2 != 0) {
            throw new DatabaseException("Hour must be even");
        }

        // ClassType-Room
        if(schedule.getClassType() == ClassType.Course && schedule.getRoom().getRoomType() != RoomType.Course) {
            throw new DatabaseException("Courses can only take place in course rooms");
        }

        if(schedule.getClassType() == ClassType.Laboratory && schedule.getRoom().getRoomType() != RoomType.Laboratory) {
            throw new DatabaseException("Laboratories can only take place in laboratory rooms");
        }

        // Discipline-Group
        if(schedule.getDiscipline().getDegree() != schedule.getSemiYear().getDegree()
        || schedule.getDiscipline().getYear() != schedule.getSemiYear().getYear()) {
            throw new DatabaseException("The specified discipline degree does not match the semi-year");
        }

        // Discipline-Teacher
        var tdManager = Database.getInstance().teacherDisciplineManager;
        boolean found = tdManager
                .getAll()
                .stream()
                .anyMatch(td -> td.getDisciplineId().equals(schedule.getDiscipline().getId())
                    && td.getTeacherId().equals(schedule.getTeacher().getId())
                );

        if(!found) {
            throw new DatabaseException("The specified teacher does not teach that discipline");
        }

        // Relative Restrictions
        for(var s : getAll()) {
            if(s.getId().equals(schedule.getId())) continue;

            // Week Restrictions
            if(s.getGroup().getIdFromAnnotation().equals(schedule.getGroup().getIdFromAnnotation())
            && s.getDiscipline().getId().equals(schedule.getDiscipline().getId())) {
                if(s.getClassType() == ClassType.Course && schedule.getClassType() == ClassType.Course) {
                    throw new DatabaseException("There is already a course scheduled this week for the specified discipline and group.");
                }

                if(s.getClassType() != ClassType.Course && schedule.getClassType() != ClassType.Course) {
                    throw new DatabaseException("There is already a seminary/laboratory scheduled this week for the specified discipline and group.");
                }
            }

            // Overlap Restrictions
            if(s.getDayOfWeek() == schedule.getDayOfWeek() && s.getStartHour() == schedule.getStartHour()) {
                if(s.getTeacher().getId().equals(schedule.getTeacher().getId())) {
                    throw new DatabaseException("The specified teacher has another class at that hour.");
                }

                if(s.getRoom().getId().equals(schedule.getRoom().getId())) {
                    throw new DatabaseException("The specified room has another class at that hour.");
                }

                // Get all faculty groups ids scheduled at that time and check collisions
                List<String> sGroupsIds = (s.getClassType() != ClassType.Course)
                        ? Stream.of((FacultyGroup) s.getGroup()).map(FacultyGroup::getId).toList()
                        : ((SemiYear) s.getGroup()).getFacultyGroups().stream().map(FacultyGroup::getId).toList();

                List<String> scheduleGroupsIds = (schedule.getClassType() != ClassType.Course)
                        ? Stream.of((FacultyGroup) schedule.getGroup()).map(FacultyGroup::getId).toList()
                        : ((SemiYear) schedule.getGroup()).getFacultyGroups().stream().map(FacultyGroup::getId).toList();

                if(sGroupsIds.stream().anyMatch(scheduleGroupsIds::contains)) {
                    throw new DatabaseException("The specified group has another class at that hour.");
                }
            }
        }
    }
}
