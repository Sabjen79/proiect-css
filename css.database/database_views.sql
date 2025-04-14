-- Create a view to easily check schedule conflicts for rooms
CREATE VIEW RoomConflicts AS
SELECT 
    s1.id AS schedule1_id,
    s2.id AS schedule2_id,
    r.name AS room_name,
    t1.day_of_week,
    t1.start_time AS time1_start,
    t1.end_time AS time1_end,
    t2.start_time AS time2_start,
    t2.end_time AS time2_end
FROM 
    Schedule s1
    JOIN Schedule s2 ON s1.room_id = s2.room_id AND s1.id < s2.id
    JOIN Room r ON s1.room_id = r.id
    JOIN TimeSlot t1 ON s1.time_slot_id = t1.id
    JOIN TimeSlot t2 ON s2.time_slot_id = t2.id
WHERE 
    t1.day_of_week = t2.day_of_week
    AND ((t1.start_time <= t2.start_time AND t1.end_time > t2.start_time)
    OR (t2.start_time <= t1.start_time AND t2.end_time > t1.start_time));

-- Create a view to easily check schedule conflicts for teachers
CREATE VIEW TeacherConflicts AS
SELECT 
    s1.id AS schedule1_id,
    s2.id AS schedule2_id,
    t.name AS teacher_name,
    ts1.day_of_week,
    ts1.start_time AS time1_start,
    ts1.end_time AS time1_end,
    ts2.start_time AS time2_start,
    ts2.end_time AS time2_end
FROM 
    Schedule s1
    JOIN Schedule s2 ON s1.teacher_id = s2.teacher_id AND s1.id < s2.id
    JOIN Teacher t ON s1.teacher_id = t.id
    JOIN TimeSlot ts1 ON s1.time_slot_id = ts1.id
    JOIN TimeSlot ts2 ON s2.time_slot_id = ts2.id
WHERE 
    ts1.day_of_week = ts2.day_of_week
    AND ((ts1.start_time <= ts2.start_time AND ts1.end_time > ts2.start_time)
    OR (ts2.start_time <= ts1.start_time AND ts2.end_time > ts1.start_time));

-- Create a view for checking room type compatibility with class type
CREATE VIEW RoomClassCompatibility AS
SELECT 
    s.id AS schedule_id,
    r.name AS room_name,
    rt.type_name AS room_type,
    ct.type_name AS class_type,
    CASE 
        WHEN (ct.type_name = 'Course' AND rt.type_name != 'Course') THEN 'Incompatible: Course must be in Course room'
        WHEN (ct.type_name = 'Laboratory' AND rt.type_name != 'Laboratory') THEN 'Incompatible: Laboratory must be in Laboratory room'
        ELSE 'Compatible'
    END AS compatibility_status
FROM 
    Schedule s
    JOIN Room r ON s.room_id = r.id
    JOIN RoomType rt ON r.room_type_id = rt.id
    JOIN ClassType ct ON s.class_type_id = ct.id
WHERE 
    (ct.type_name = 'Course' AND rt.type_name != 'Course')
    OR (ct.type_name = 'Laboratory' AND rt.type_name != 'Laboratory');

-- Create a view for group scheduling
CREATE VIEW GroupSchedule AS
SELECT 
    g.name AS group_name,
    sy.name AS study_year,
    d.name AS discipline,
    ct.type_name AS class_type,
    t.name AS teacher,
    r.name AS room,
    ts.day_of_week,
    ts.start_time,
    ts.end_time
FROM 
    Schedule s
    JOIN `Group` g ON s.group_id = g.id
    JOIN StudyYear sy ON g.study_year_id = sy.id
    JOIN Discipline d ON s.discipline_id = d.id
    JOIN ClassType ct ON s.class_type_id = ct.id
    JOIN Teacher t ON s.teacher_id = t.id
    JOIN Room r ON s.room_id = r.id
    JOIN TimeSlot ts ON s.time_slot_id = ts.id
ORDER BY 
    g.name, ts.day_of_week, ts.start_time;

-- Create a view for study year course schedule
CREATE VIEW StudyYearCourseSchedule AS
SELECT 
    sy.name AS study_year,
    d.name AS discipline,
    t.name AS teacher,
    r.name AS room,
    ts.day_of_week,
    ts.start_time,
    ts.end_time
FROM 
    Schedule s
    JOIN StudyYear sy ON s.study_year_id = sy.id
    JOIN Discipline d ON s.discipline_id = d.id
    JOIN ClassType ct ON s.class_type_id = ct.id
    JOIN Teacher t ON s.teacher_id = t.id
    JOIN Room r ON s.room_id = r.id
    JOIN TimeSlot ts ON s.time_slot_id = ts.id
WHERE 
    s.group_id IS NULL
    AND ct.type_name = 'Course'
ORDER BY 
    sy.name, ts.day_of_week, ts.start_time;