package fii.css.database.persistence.entities;

public class TimeSlot {
    private int timeSlotId;
    private DayOfWeek dayOfWeek;
    private String endTime;
    private String startTime;

    public TimeSlot(int timeSlotId, DayOfWeek dayOfWeek, String endTime, String startTime) {
        this.timeSlotId = timeSlotId;
        this.dayOfWeek = dayOfWeek;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
