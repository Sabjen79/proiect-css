------------------------------------------------------------------------------

CREATE TABLE Discipline (
    discipline_id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    year INT NOT NULL,
    study_year_id TEXT NOT NULL
);

CREATE TABLE Teacher (
    teacher_id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    title TEXT NOT NULL
);

CREATE TABLE TeacherDiscipline (
    teacher_discipline_id TEXT PRIMARY KEY,
    teacher_id TEXT NOT NULL,
    discipline_id TEXT NOT NULL
);

------------------------------------------------------------------------------

CREATE TABLE Room (
    room_id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    capacity INTEGER NOT NULL,
    room_type INTEGER NOT NULL -- Hardcoded value
);

------------------------------------------------------------------------------

CREATE TABLE StudyYear (
    study_year_id TEXT PRIMARY KEY,
    degree INTEGER NOT NULL, -- Hardcoded value for Bachelors, Masters, PhD
    specialty TEXT NOT NULL,
    max_years INTEGER NOT NULL
);

CREATE TABLE FacultyGroup (
    faculty_group_id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    year INTEGER NOT NULL,
    study_year_id INTEGER NOT NULL
);

------------------------------------------------------------------------------

CREATE TABLE Schedule (
    schedule_id TEXT PRIMARY KEY,
    teacher_discipline_id INTEGER NOT NULL, -- Contains a teacher and a discipline
    class_type INTEGER NOT NULL, -- Hardcoded value
    room_id INTEGER NOT NULL,
    faculty_group_id INTEGER NOT NULL, -- Contains the year, specialty and group
    day_of_week INTEGER NOT NULL, -- Hardcoded value
    start_hour INTEGER NOT NULL,
    end_hour INTEGER NOT NULL
);

-- indexes
CREATE INDEX idx_schedule_teacher_discipline ON Schedule(teacher_discipline_id);
CREATE INDEX idx_schedule_room ON Schedule(room_id);
CREATE INDEX idx_group_faculty_group ON FacultyGroup(faculty_group_id);