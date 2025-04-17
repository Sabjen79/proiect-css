package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("Schedule")
public class Schedule extends DatabaseEntity {
    @Id
    @Column("schedule_id")
    private int scheduleId;

    @Column("discipline_id")
    private int disciplineId;

    @Column("teacher_id")
    private int teacherId;

    @Column("class_type_id")
    private int classTypeId;

    @Column("room_id")
    private int roomId;

    @Column("time_slot_id")
    private int timeSlotId;

    @Column("study_year_id")
    private int studyYearId;

    @Column("faculty_group_id")
    private int facultyGroupId;

    public Schedule() {}

    public int getFacultyGroupId() {
        return facultyGroupId;
    }

    public void setFacultyGroupId(int facultyGroupId) {
        this.facultyGroupId = facultyGroupId;
    }

    public int getStudyYearId() {
        return studyYearId;
    }

    public void setStudyYearId(int studyYearId) {
        this.studyYearId = studyYearId;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(int classTypeId) {
        this.classTypeId = classTypeId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

}
