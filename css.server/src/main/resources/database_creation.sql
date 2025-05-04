------------------------------------------------------------------------------

CREATE TABLE Room (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    capacity INTEGER NOT NULL,
    room_type INTEGER NOT NULL -- Hardcoded value
);

------------------------------------------------------------------------------

CREATE TABLE SemiYear (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    degree INTEGER NOT NULL, -- Hardcoded value for Bachelors, Masters
    year INTEGER NOT NULL
);

CREATE TABLE FacultyGroup (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    semi_year_id TEXT NOT NULL
);

------------------------------------------------------------------------------

CREATE TABLE Discipline (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    degree INTEGER NOT NULL,
    year INTEGER NOT NULL
);

CREATE TABLE Teacher (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    title TEXT NOT NULL
);

CREATE TABLE TeacherDiscipline (
    id TEXT PRIMARY KEY,
    teacher_id TEXT NOT NULL,
    discipline_id TEXT NOT NULL
);

------------------------------------------------------------------------------

CREATE TABLE Schedule (
    id TEXT PRIMARY KEY,
    teacher_id TEXT NOT NULL,
    discipline_id TEXT NOT NULL,
    class_type INTEGER NOT NULL, -- Hardcoded value
    students_id TEXT NOT NULL, -- References a SemiYear for COURSES, or FacultyGroup for LABS/SEMINARIES
    room_id TEXT NOT NULL,
    day_of_week INTEGER NOT NULL, -- Hardcoded value
    start_hour INTEGER NOT NULL
);

-- indexes
CREATE INDEX idx_schedule_teacher_discipline ON Schedule(id);
CREATE INDEX idx_schedule_room ON Schedule(id);
CREATE INDEX idx_group_faculty_group ON FacultyGroup(id);