package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("TimeSlot")
public class TimeSlot extends DatabaseEntity {
    @Id
    @Column("time_slot_id")
    private int timeSlotId;

    @Column("day_of_week")
    private DayOfWeek dayOfWeek;

    @Column("end_time")
    private String endTime;

    @Column("start_time")
    private String startTime;

    public TimeSlot() {}

    public int getTimeSlotId() {
        return timeSlotId;
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
