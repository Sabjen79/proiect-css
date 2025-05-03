package fii.css.database.persistence.managers;

import fii.css.database.Database;
import fii.css.database.persistence.entities.*;
import fii.css.database.persistence.repositories.ScheduleRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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


    public Schedule addSchedule(
            TeacherDiscipline teacherDiscipline,
            ClassType classType,
            Room room,
            FacultyGroup facultyGroup,
            DayOfWeek dayOfWeek,
            int startHour,
            int endHour
    ) {
        validateSchedule(teacherDiscipline, classType, room, facultyGroup, dayOfWeek, startHour, endHour);

        var entity = repository.newEntity();
        entity.setTeacherDiscipline(teacherDiscipline);
        entity.setClassType(classType);
        entity.setRoom(room);
        entity.setFacultyGroup(facultyGroup);
        entity.setDayOfWeek(dayOfWeek);
        entity.setStartHour(startHour);
        entity.setEndHour(endHour);

        repository.persist(entity);

        return entity;
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
        var entity = repository.getById(id);
        if (entity == null) {
            throw new RuntimeException("Schedule with ID " + id + " does not exist.");
        }

        validateSchedule(teacherDiscipline, classType, room, facultyGroup, dayOfWeek, startHour, endHour, id);

        entity.setTeacherDiscipline(teacherDiscipline);
        entity.setClassType(classType);
        entity.setRoom(room);
        entity.setFacultyGroup(facultyGroup);
        entity.setDayOfWeek(dayOfWeek);
        entity.setStartHour(startHour);
        entity.setEndHour(endHour);

        repository.merge(entity);

        return entity;
    }

    @Override
    public void remove(String id) {
        repository.delete(repository.getById(id));
    }

    private void validateSchedule(
            TeacherDiscipline teacherDiscipline,
            ClassType classType,
            Room room,
            FacultyGroup facultyGroup,
            DayOfWeek dayOfWeek,
            int startHour,
            int endHour
    ) {
        validateSchedule(teacherDiscipline, classType, room, facultyGroup, dayOfWeek, startHour, endHour, null);
    }

    private void validateSchedule(
            TeacherDiscipline teacherDiscipline,
            ClassType classType,
            Room room,
            FacultyGroup facultyGroup,
            DayOfWeek dayOfWeek,
            int startHour,
            int endHour,
            String excludeId
    ) {
        if (dayOfWeek == DayOfWeek.Saturday || dayOfWeek == DayOfWeek.Sunday) {
            throw new RuntimeException("Classes may only be scheduled from Monday to Friday.");
        }

        if (startHour < 8 || endHour > 20 || startHour >= endHour) {
            throw new RuntimeException("Classes must be scheduled between 8:00 and 20:00, and startHour must be less than endHour.");
        }

        // I added exams to class types, and labs, seminaries and courses should be 2 hours long
        if (endHour - startHour < 2 && (classType == ClassType.Course || classType == ClassType.Seminary || classType == ClassType.Laboratory)) {
            throw new RuntimeException("A schedule should be made for two hours.");
        }

        if (classType == ClassType.Course && room.getRoomType() != RoomType.Course) {
            throw new RuntimeException("Courses must be scheduled in rooms of type Course.");
        }

        if (classType == ClassType.Laboratory && room.getRoomType() != RoomType.Laboratory) {
            throw new RuntimeException("Laboratories must be scheduled in rooms of type Laboratory.");
        }

        // validate that there is no overlap
        for (Schedule schedule : repository.getAll()) {
            if (excludeId != null && schedule.getId().equals(excludeId)) {
                continue;
            }

            if (schedule.getDayOfWeek() != dayOfWeek) {
                continue;
            }

            boolean timeOverlap = startHour < schedule.getEndHour() && endHour > schedule.getStartHour();

            if (timeOverlap) {
                // same room conflict
                if (schedule.getRoom().getId().equals(room.getId())) {
                    throw new RuntimeException("Room is already booked at this time.");
                }

                // same teacher conflict
                if (schedule.getTeacherDiscipline().getTeacher().getId()
                        .equals(teacherDiscipline.getTeacher().getId())) {
                    throw new RuntimeException("Teacher is already scheduled at this time.");
                }

                // same group conflict (only for labs and seminars, not for courses)
                if (classType != ClassType.Course && schedule.getFacultyGroup().getId().equals(facultyGroup.getId())) {
                    throw new RuntimeException("Faculty group is already scheduled at this time.");
                }
            }
        }

        // Obține toate grupele asociate anului de studiu
        int year = teacherDiscipline.getDiscipline().getYear();
        List<FacultyGroup> groupsForYear = Database.getInstance()
                .facultyGroupManager
                .getAll()
                .stream()
                .filter(group -> group.getYear() == year)
                .toList();

        if (classType == ClassType.Laboratory || classType == ClassType.Seminary) {
            // Verifică dacă există un laborator/seminar pentru fiecare grupă
            for (FacultyGroup group : groupsForYear) {
                long count = Database.getInstance()
                        .scheduleManager
                        .getAll()
                        .stream()
                        .filter(schedule -> schedule.getTeacherDiscipline().getId().equals(teacherDiscipline.getId()))
                        .filter(schedule -> schedule.getClassType() == classType)
                        .filter(schedule -> schedule.getFacultyGroup().getId().equals(group.getId()))
                        .count();

                // TODO: please check my logic, i am tired and this may be very stupid
//                if (excludeId!=null && count == 0) {
//                    throw new RuntimeException("Each group must have at least one " + classType + " scheduled.");
//                }
            }
        } else if (classType == ClassType.Course) {
            // Verifică dacă toate grupele dintr-un prefix sunt libere pentru curs
            Set<String> prefixes = groupsForYear.stream()
                    .map(group -> group.getGroupName().substring(0, 1).toUpperCase())
                    .collect(Collectors.toSet());

            for (String prefix : prefixes) {
                boolean allGroupsFree = groupsForYear.stream()
                        .filter(group -> group.getGroupName().startsWith(prefix))
                        .allMatch(group -> Database.getInstance()
                                .scheduleManager
                                .getAll()
                                .stream()
                                .noneMatch(schedule -> schedule.getFacultyGroup().getId().equals(group.getId()) &&
                                        schedule.getClassType() == ClassType.Laboratory &&
                                        schedule.getTeacherDiscipline().getId().equals(teacherDiscipline.getId())));

//                if (!allGroupsFree) {
//                    throw new RuntimeException("Cannot schedule course: not all groups with prefix " + prefix + " are free.");
//                }
            }
        }

        //verifica ca grupa are acelasi an ca si materia la ca care vreau sa o programez
        String goupsId = facultyGroup.getStudyYear().getId();
        String studyYearIdFromTeacher = teacherDiscipline.getDiscipline().getStudyYearId();
        //caut studyYear care are id ul asta si vedem ce specializare are


        System.out.println("groups year: " + facultyGroup.getYear());
        System.out.println("disciplines year: " + year);
        //&& !Objects.equals(goupsId, studyYearIdFromTeacher)
        if (facultyGroup.getYear() != year ) {
            throw new RuntimeException("The faculty group year does not match the discipline year.");
        }

        if(!Objects.equals(goupsId, studyYearIdFromTeacher)){
            throw new RuntimeException("The faculty group specialty does not match the discipline specialty.");
        }




        // courses should be scheduled at study year level, not group level
        if (classType == ClassType.Course) {
            // TODO: please check my logic, i am tired and this may be very stupid
            // am presupus ca pot planifica un curs la un semian daca toate grupele din acel seminar sunt libere
            // practic, trec prin toate grupele care incep cu litera de care am nevoie si vad daca sunt toate libere
            // daca nu sunt, nu planific programarea din orar
            // TODO: write a test to check if this works properly
            String groupPrefix = facultyGroup.getGroupName().substring(0, 1).toUpperCase();

            // find all groups with the same prefix
            List<FacultyGroup> groupsWithSamePrefix = Database
                    .getInstance()
                    .facultyGroupManager
                    .getAll()
                    .stream()
                    .filter(group -> group.getGroupName().startsWith(groupPrefix))
                    .toList();

            for (FacultyGroup group : groupsWithSamePrefix) {
                boolean overlapExists = Database
                        .getInstance()
                        .scheduleManager
                        .getAll()
                        .stream()
                        .filter(schedule -> schedule.getFacultyGroup().getId().equals(group.getId()))
                        .anyMatch(schedule ->
                                schedule.getDayOfWeek() == dayOfWeek &&
                                        (
                                                (startHour >= schedule.getStartHour() && startHour < schedule.getEndHour()) ||
                                                        (endHour > schedule.getStartHour() && endHour <= schedule.getEndHour()) ||
                                                        (startHour <= schedule.getStartHour() && endHour >= schedule.getEndHour())
                                        )
                        );
                if (overlapExists) {
                    throw new RuntimeException("Cannot schedule course: group " + group.getGroupName() + " is busy during this time.");
                }
            }
        }
    }
}
