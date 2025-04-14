DROP DATABASE IF EXISTS faculty_timetable;

--create database
CREATE DATABASE faculty_timetable;
USE faculty_timetable;


CREATE TABLE StudyYear (
    study_year_id INT AUTO_INCREMENT PRIMARY KEY,
    value VARCHAR(255) NOT NULL
);

CREATE TABLE Discipline (
    discipline_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE Teacher (
    teacher_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    title VARCHAR(50)
);

CREATE TABLE ClassType (
    class_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL -- seminary, lab, course
);

CREATE TABLE TimeSlot (
    time_slot_id INT AUTO_INCREMENT PRIMARY KEY,
    day_of_week ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CHECK (start_time >= '08:00:00' AND end_time <= '20:00:00')
);

CREATE TABLE RoomType (
    room_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL
);

CREATE TABLE Room (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    room_type_id INT NOT NULL,
    FOREIGN KEY (room_type_id) REFERENCES RoomType(id)
);

CREATE TABLE FacultyGroup (
    group_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    study_year_id INT NOT NULL,
    FOREIGN KEY (study_year_id) REFERENCES StudyYear(id) ON DELETE CASCADE
);

CREATE TABLE Schedule (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    discipline_id INT NOT NULL,
    teacher_id INT NOT NULL,
    class_type_id INT NOT NULL,
    room_id INT NOT NULL,
    time_slot_id INT NOT NULL,
    study_year_id INT NOT NULL,
    faculty_group_id INT,
    FOREIGN KEY (discipline_id) REFERENCES Discipline(id),
    FOREIGN KEY (teacher_id) REFERENCES Teacher(id),
    FOREIGN KEY (class_type_id) REFERENCES ClassType(id),
    FOREIGN KEY (room_id) REFERENCES Room(id),
    FOREIGN KEY (time_slot_id) REFERENCES TimeSlot(id),
    FOREIGN KEY (study_year_id) REFERENCES StudyYear(id),
    FOREIGN KEY (faculty_group_id) REFERENCES FacultyGroup(id)
);

-- default values for roomtype
INSERT INTO RoomType (type_name) VALUES 
('Course'),
('Laboratory'),
('Seminary');

-- default values for classtype
INSERT INTO ClassType (type_name) VALUES 
('Course'),
('Laboratory'),
('Seminary');

-- indexes to help with query performance
CREATE INDEX idx_schedule_teacher ON Schedule(teacher_id);
CREATE INDEX idx_schedule_room ON Schedule(room_id);
CREATE INDEX idx_schedule_timeslot ON Schedule(time_slot_id);
CREATE INDEX idx_schedule_discipline ON Schedule(discipline_id);
CREATE INDEX idx_group_study_year ON FacultyGroup(study_year_id);
CREATE INDEX idx_room_type ON Room(room_type_id);
