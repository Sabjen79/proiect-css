package fii.css;

import fii.css.api.RestApi;
import fii.css.database.Database;
import fii.css.database.persistence.entities.ClassType;
import fii.css.database.persistence.entities.DayOfWeek;
import fii.css.database.persistence.entities.Degree;
import fii.css.database.persistence.entities.RoomType;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database database = Database.getInstance();
        database.initialize();

        // TODO: After every manager is completed, this example should run without errors
        example(database);

        RestApi api = new RestApi();
        api.start();
    }

    private static void example(Database database) {
        var discipline = database.disciplineManager.addDiscipline("Materie", "O materie");
        database.disciplineManager.updateDiscipline(discipline.getId(), discipline.getName(), "O materie faina");

        var teacher = database.teacherManager.addTeacher("Ilie", "Nervosul");
        database.teacherManager.updateTeacher(teacher.getId(), teacher.getName(), "Nervosu");

        var teacherDiscipline = database.teacherDisciplineManager.createTeacherDiscipline(teacher, discipline);
        database.teacherDisciplineManager.updateTeacherDiscipline(teacherDiscipline.getId(), teacher, discipline);

        var room = database.roomManager.addRoom("C2", 200, RoomType.Laboratory);
        database.roomManager.updateRoom(room.getId(), room.getName(), 400, RoomType.Course);

        var studyYear = database.studyYearManager.addStudyYear(Degree.Bachelor, "ISS", 3);
        database.studyYearManager.updateStudyYear(studyYear.getId(), Degree.Master, studyYear.getSpecialty(), 2);

        var facultyGroup = database.facultyGroupManager.addFacultyGroup("A1", 1, studyYear);
        database.facultyGroupManager.updateFacultyGroup(facultyGroup.getId(), "X1", facultyGroup.getYear(), facultyGroup.getStudyYear());

        var schedule = database.scheduleManager.addSchedule(
                teacherDiscipline,
                ClassType.Course,
                room,
                facultyGroup,
                DayOfWeek.Friday,
                16, 18
        );
        database.scheduleManager.updateSchedule(
                schedule.getId(),
                teacherDiscipline,
                ClassType.Seminary,
                room,
                facultyGroup,
                DayOfWeek.Saturday,
                8, 10
        );

        // Remove teacher -> teacherDiscipline -> schedule
        database.teacherManager.remove(teacher.getId());

        // Remove StudyYear -> FacultyGroup
        database.studyYearManager.remove(studyYear.getId());

        // Remove leftovers
        database.disciplineManager.remove(discipline.getId());
        database.roomManager.removeRoom(room.getId());
    }
}
