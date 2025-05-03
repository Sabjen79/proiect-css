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

    @Column("teacher_id")
    private String teacherId;

    @Column("discipline_id")
    private String disciplineId;

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

    public Teacher getTeacher() {
        return Database
                .getInstance()
                .teacherManager
                .get(teacherId);
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Discipline getDiscipline() {
        return Database
                .getInstance()
                .disciplineManager
                .get(disciplineId);
    }

    public void setDisciplineId(String disciplineId) {
        this.disciplineId = disciplineId;
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

    public void setRoomId(String id) {
        this.roomId = id;
    }

    public FacultyGroup getFacultyGroup() {
        return Database
            .getInstance()
            .facultyGroupManager
            .get(facultyGroupId);
    }

    public void setFacultyGroupId(String id) {
        this.facultyGroupId = id;
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

    @Override
    public DatabaseEntity clone() {
        var schedule = new Schedule();

        schedule.scheduleId = this.scheduleId;
        schedule.teacherId = this.teacherId;
        schedule.disciplineId = this.disciplineId;
        schedule.classType = this.classType;
        schedule.roomId = this.roomId;
        schedule.facultyGroupId = this.facultyGroupId;
        schedule.dayOfWeek = this.dayOfWeek;
        schedule.startHour = this.startHour;
        schedule.endHour = this.endHour;

        return schedule;
    }
}
