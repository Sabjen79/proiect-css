package fii.css.database.persistence.entities;



public class Schedule {
    private int scheduleId;
    private int disciplineId;
    private int teacherId;
    private int classTypeId;
    private int roomId;
    private int timeSlotId;
    private int studyYearId;
    private int facultyGroupId;

    public Schedule(int scheduleId, int disciplineId, int teacherId, int classTypeId, int roomId, int timeSlotId, int studyYearId, int facultyGroupId) {
        this.scheduleId = scheduleId;
        this.disciplineId = disciplineId;
        this.teacherId = teacherId;
        this.classTypeId = classTypeId;
        this.roomId = roomId;
        this.timeSlotId = timeSlotId;
        this.studyYearId = studyYearId;
        this.facultyGroupId = facultyGroupId;
    }

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

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
