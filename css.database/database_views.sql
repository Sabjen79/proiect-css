-- Create a view to easily check schedule conflicts for rooms
CREATE VIEW IF NOT EXISTS RoomConflicts AS
SELECT 
    s1.schedule_id AS schedule1_id,
    s2.schedule_id AS schedule2_id,
    r.name AS room_name,
    t1.day_of_week,
    t1.start_time AS time1_start,
    t1.end_time AS time1_end,
    t2.start_time AS time2_start,
    t2.end_time AS time2_end
FROM 
    Schedule s1
    JOIN Schedule s2 ON s1.room_id = s2.room_id AND s1.schedule_id < s2.schedule_id
    JOIN Room r ON s1.room_id = r.room_id
    JOIN TimeSlot t1 ON s1.time_slot_id = t1.time_slot_id
    JOIN TimeSlot t2 ON s2.time_slot_id = t2.time_slot_id
WHERE 
    t1.day_of_week = t2.day_of_week
    AND ((t1.start_time <= t2.start_time AND t1.end_time > t2.start_time)
    OR (t2.start_time <= t1.start_time AND t2.end_time > t1.start_time));

-- Create a view to easily check schedule conflicts for teachers
CREATE VIEW IF NOT EXISTS TeacherConflicts AS
SELECT 
    s1.schedule_id AS schedule1_id,
    s2.schedule_id AS schedule2_id,
    t.name AS teacher_name,
    ts1.day_of_week,
    ts1.start_time AS time1_start,
    ts1.end_time AS time1_end,
    ts2.start_time AS time2_start,
    ts2.end_time AS time2_end
FROM 
    Schedule s1
    JOIN Schedule s2 ON s1.teacher_id = s2.teacher_id AND s1.schedule_id < s2.schedule_id
    JOIN Teacher t ON s1.teacher_id = t.teacher_id
    JOIN TimeSlot ts1 ON s1.time_slot_id = ts1.time_slot_id
    JOIN TimeSlot ts2 ON s2.time_slot_id = ts2.time_slot_id
WHERE 
    ts1.day_of_week = ts2.day_of_week
    AND ((ts1.start_time <= ts2.start_time AND ts1.end_time > ts2.start_time)
    OR (ts2.start_time <= ts1.start_time AND ts2.end_time > ts1.start_time));

-- Create a view for checking room type compatibility with class type
CREATE VIEW IF NOT EXISTS RoomClassCompatibility AS
SELECT 
    s.schedule_id AS schedule_id,
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
    JOIN Room r ON s.room_id = r.room_id
    JOIN RoomType rt ON r.room_type_id = rt.room_type_id
    JOIN ClassType ct ON s.class_type_id = ct.class_type_id
WHERE 
    (ct.type_name = 'Course' AND rt.type_name != 'Course')
    OR (ct.type_name = 'Laboratory' AND rt.type_name != 'Laboratory');

-- Create a view for group scheduling
CREATE VIEW IF NOT EXISTS GroupSchedule AS
SELECT 
    g.name AS group_name,
    sy.value AS study_year,
    d.name AS discipline,
    ct.type_name AS class_type,
    t.name AS teacher,
    r.name AS room,
    ts.day_of_week,
    ts.start_time,
    ts.end_time
FROM 
    Schedule s
    JOIN FacultyGroup g ON s.faculty_group_id = g.faculty_group_id
    JOIN StudyYear sy ON g.study_year_id = sy.study_year_id
    JOIN Discipline d ON s.discipline_id = d.discipline_id
    JOIN ClassType ct ON s.class_type_id = ct.class_type_id
    JOIN Teacher t ON s.teacher_id = t.teacher_id
    JOIN Room r ON s.room_id = r.room_id
    JOIN TimeSlot ts ON s.time_slot_id = ts.time_slot_id
ORDER BY 
    g.name, ts.day_of_week, ts.start_time;

-- Create a view for study year course schedule
CREATE VIEW IF NOT EXISTS StudyYearCourseSchedule AS
SELECT 
    sy.value AS study_year,
    d.name AS discipline,
    t.name AS teacher,
    r.name AS room,
    ts.day_of_week,
    ts.start_time,
    ts.end_time
FROM 
    Schedule s
    JOIN StudyYear sy ON s.study_year_id = sy.study_year_id
    JOIN Discipline d ON s.discipline_id = d.discipline_id
    JOIN ClassType ct ON s.class_type_id = ct.class_type_id
    JOIN Teacher t ON s.teacher_id = t.teacher_id
    JOIN Room r ON s.room_id = r.room_id
    JOIN TimeSlot ts ON s.time_slot_id = ts.time_slot_id
WHERE 
    s.faculty_group_id IS NULL
    AND ct.type_name = 'Course'
ORDER BY 
    sy.value, ts.day_of_week, ts.start_time;
