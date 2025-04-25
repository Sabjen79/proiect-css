package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("Schedule")
public class Schedule extends DatabaseEntity {
    @Id
    @Column("schedule_id")
    private String scheduleId;

    @Column("teacher_discipline_id")
    private String teacherDisciplineId;

    @Column("class_type")
    private int classType;

    @Column("room_id")
    private String roomId;

    @Column("faculty_group_id")
    private String facultyGroupId;

    @Column("day_of_week")
    private int dayOfWeek;

    @Column("start_hour")
    private int startHour;

    @Column("end_hour")
    private int endHour;

    public Schedule() {}

    public String getId() { return scheduleId; }

    public TeacherDiscipline getTeacherDiscipline() {
        return Database
            .getInstance()
            .teacherDisciplineManager
            .get(teacherDisciplineId);
    }

    public void setTeacherDiscipline(TeacherDiscipline teacherDiscipline) {
        this.teacherDisciplineId = teacherDiscipline.getId();
    }

    public ClassType getClassType() {
        return ClassType.fromValue(classType);
    }

    public void setClassType(ClassType classType) {
        this.classType = classType.value;
    }

    public Room getRoom() {
        return Database
            .getInstance()
            .roomManager
            .get(roomId);
    }

    public void setRoom(Room room) {
        this.roomId = room.getId();
    }

    public FacultyGroup getFacultyGroup() {
        return Database
            .getInstance()
            .facultyGroupManager
            .get(facultyGroupId);
    }

    public void setFacultyGroup(FacultyGroup facultyGroup) {
        this.facultyGroupId = facultyGroup.getId();
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.fromValue(dayOfWeek);
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek.value;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
}
