package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.DayOfWeek;
import fii.css.database.persistence.entities.TimeSlot;
import fii.css.database.persistence.repositories.TimeSlotRepository;

import java.time.LocalTime;

public class TimeSlotManager extends AbstractEntityManager<TimeSlot> {
    public TimeSlotManager() {super(new TimeSlotRepository());}

    public TimeSlot addTimeSlot(DayOfWeek dayOfWeek, String startTime, String endTime) {
        var entity = repository.newEntity();

        checkTimeSlotValidity(dayOfWeek, startTime, endTime, entity);

        repository.persist(entity);

        return entity;

    }

    public TimeSlot updateTimeSlot(int id, DayOfWeek dayOfWeek, String startTime, String endTime) {
        var entity = repository.getById(id);

        checkTimeSlotValidity(dayOfWeek, startTime, endTime, entity);

        repository.merge(entity);

        return entity;

    }

    private void checkTimeSlotValidity(DayOfWeek dayOfWeek, String startTime, String endTime, TimeSlot entity) {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        if (start.isBefore(LocalTime.of(8, 0)) || end.isAfter(LocalTime.of(20, 0))) {
            throw new RuntimeException("TimeSlot must be between 08:00 and 20:00");
        }

        if (!start.isBefore(end)) {
            throw new RuntimeException("Start time must be before end time");
        }

        boolean isDuplicate = repository.getAll().stream().anyMatch(slot ->
                slot.getDayOfWeek().equals(dayOfWeek)
                        && slot.getStartTime().equals(startTime)
                        && slot.getEndTime().equals(endTime)
        );

        if (isDuplicate) {
            throw new RuntimeException("TimeSlot already exists for " + dayOfWeek + " from " + startTime + " to " + endTime);
        }

        entity.setDayOfWeek(dayOfWeek);
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);
    }

    public void removeTimeSlot(int id) {repository.delete(repository.getById(id));}
}
