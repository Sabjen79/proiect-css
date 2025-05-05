package fii.css.database.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("Schedule")
public class Schedule extends DatabaseEntity {
    @Id
    @Column("id")
    private String id;

    @Column("teacher_id")
    private String teacherId;

    @Column("discipline_id")
    private String disciplineId;

    @Column("class_type")
    private int classType;

    @Column("room_id")
    private String roomId;

    @Column("students_id")
    private String students_id;

    @Column("day_of_week")
    private int dayOfWeek;

    @Column("start_hour")
    private int startHour;

    public Schedule() {}

    public String getId() { return id; }

    @JsonIgnore
    public Teacher getTeacher() {
        return Database
                .getInstance()
                .teacherManager
                .get(teacherId);
    }

    public String getTeacherId() { return teacherId; }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @JsonIgnore
    public Discipline getDiscipline() {
        return Database
                .getInstance()
                .disciplineManager
                .get(disciplineId);
    }

    public String getDisciplineId() { return disciplineId; }

    public void setDisciplineId(String disciplineId) {
        this.disciplineId = disciplineId;
    }

    public ClassType getClassType() {
        return ClassType.fromValue(classType);
    }

    public void setClassType(ClassType classType) {
        this.classType = classType.value;
    }

    @JsonIgnore
    public Room getRoom() {
        return Database
            .getInstance()
            .roomManager
            .get(roomId);
    }

    public String getRoomId() { return roomId; }

    public void setRoomId(String id) {
        this.roomId = id;
    }

    @JsonIgnore
    public DatabaseEntity getGroup() {
        var manager = (getClassType() == ClassType.Course)
                ? Database.getInstance().semiYearManager
                : Database.getInstance().facultyGroupManager;

        return manager.get(students_id);
    }

    @JsonIgnore
    public SemiYear getSemiYear() {
        return (getClassType() == ClassType.Course)
                ? (SemiYear) getGroup()
                : Database.getInstance().semiYearManager.get(((FacultyGroup) getGroup()).getSemiYearId());
    }

    public String getStudentsId() {
        return students_id;
    }

    public void setStudentsId(String id) {
        this.students_id = id;
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

    @Override
    public DatabaseEntity clone() {
        var schedule = new Schedule();

        schedule.id = this.id;
        schedule.teacherId = this.teacherId;
        schedule.disciplineId = this.disciplineId;
        schedule.classType = this.classType;
        schedule.roomId = this.roomId;
        schedule.students_id = this.students_id;
        schedule.dayOfWeek = this.dayOfWeek;
        schedule.startHour = this.startHour;

        return schedule;
    }
}
