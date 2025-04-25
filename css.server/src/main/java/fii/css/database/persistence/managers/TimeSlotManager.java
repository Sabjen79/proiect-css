package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.DayOfWeek;
import fii.css.database.persistence.entities.TimeSlot;
import fii.css.database.persistence.repositories.AbstractRepository;

import java.time.LocalTime;

public class TimeSlotManager extends AbstractEntityManager<TimeSlot> {
    public TimeSlotManager(AbstractRepository<TimeSlot> repo) {
        super(repo);
    }

    public TimeSlot addTimeSlot(DayOfWeek day, String startTime, String endTime) {
        validateDay(day);
        validateTime(startTime, endTime);

        var entity = repository.newEntity();
        entity.setDayOfWeek(day);
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);

        repository.persist(entity);
        return entity;
    }

    public TimeSlot updateTimeSlot(int id, DayOfWeek day, String startTime, String endTime) {
        var entity = repository.getById(id);
        if (entity == null) throw new RuntimeException("No timeslot with ID " + id);

        validateDay(day);
        validateTime(startTime, endTime);

        entity.setDayOfWeek(day);
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);

        repository.merge(entity);
        return entity;
    }

    public void removeTimeSlot(int id) {
        repository.delete(repository.getById(id));
    }

    private void validateDay(DayOfWeek day) {
        if (day.ordinal() < 1|| day.ordinal() > 5) {
            throw new RuntimeException("Invalid day: " + day + ". Must be Monday to Friday.");
        }
    }

    private void validateTime(String start, String end) {
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);

        LocalTime min = LocalTime.of(8, 0);
        LocalTime max = LocalTime.of(20, 0);

        if (startTime.isBefore(min) || endTime.isAfter(max)) {
            throw new RuntimeException("Time must be between 08:00:00 and 20:00:00.");
        }

        if (!startTime.isBefore(endTime)) {
            throw new RuntimeException("Start time must be before end time.");
        }
    }
}
