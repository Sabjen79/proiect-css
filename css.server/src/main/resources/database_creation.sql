PRAGMA foreign_keys = ON;

CREATE TABLE StudyYear (
                           study_year_id INTEGER PRIMARY KEY,
                           value TEXT NOT NULL
);

CREATE TABLE Discipline (
                            discipline_id INTEGER PRIMARY KEY,
                            name TEXT NOT NULL,
                            description TEXT
);

CREATE TABLE Teacher (
                         teacher_id INTEGER PRIMARY KEY,
                         name TEXT NOT NULL,
                         title TEXT
);

CREATE TABLE TeacherDiscipline (
                        teacher_discipline_id INTEGER PRIMARY KEY,
                        teacher_id INTEGER NOT NULL,
                        discipline_id INTEGER NOT NULL,
                        FOREIGN KEY (teacher_id) REFERENCES Teacher(teacher_id) ON DELETE CASCADE,
                        FOREIGN KEY (discipline_id) REFERENCES Discipline(discipline_id) ON DELETE CASCADE
);

CREATE TABLE ClassType (
                           class_type_id INTEGER PRIMARY KEY,
                           type_name TEXT NOT NULL
);

CREATE TABLE TimeSlot (
                          time_slot_id INTEGER PRIMARY KEY,
                          day_of_week TEXT NOT NULL CHECK(day_of_week IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday')),
                          start_time TEXT NOT NULL,
                          end_time TEXT NOT NULL,
                          CHECK (start_time >= '08:00:00' AND end_time <= '20:00:00')
);

CREATE TABLE RoomType (
                          room_type_id INTEGER PRIMARY KEY,
                          type_name TEXT NOT NULL
);

CREATE TABLE Room (
                      room_id INTEGER PRIMARY KEY,
                      name TEXT NOT NULL,
                      capacity INTEGER NOT NULL,
                      room_type_id INTEGER NOT NULL,
                      FOREIGN KEY (room_type_id) REFERENCES RoomType(room_type_id)
);

CREATE TABLE FacultyGroup (
                              faculty_group_id INTEGER PRIMARY KEY,
                              name TEXT NOT NULL,
                              study_year_id INTEGER NOT NULL,
                              FOREIGN KEY (study_year_id) REFERENCES StudyYear(study_year_id) ON DELETE CASCADE
);

CREATE TABLE Schedule (
                          schedule_id INTEGER PRIMARY KEY,
                          discipline_id INTEGER NOT NULL,
                          teacher_id INTEGER NOT NULL,
                          class_type_id INTEGER NOT NULL,
                          room_id INTEGER NOT NULL,
                          time_slot_id INTEGER NOT NULL,
                          study_year_id INTEGER NOT NULL,
                          faculty_group_id INTEGER,
                          FOREIGN KEY (discipline_id) REFERENCES Discipline(discipline_id),
                          FOREIGN KEY (teacher_id) REFERENCES Teacher(teacher_id),
                          FOREIGN KEY (class_type_id) REFERENCES ClassType(class_type_id),
                          FOREIGN KEY (room_id) REFERENCES Room(room_id),
                          FOREIGN KEY (time_slot_id) REFERENCES TimeSlot(time_slot_id),
                          FOREIGN KEY (study_year_id) REFERENCES StudyYear(study_year_id),
                          FOREIGN KEY (faculty_group_id) REFERENCES FacultyGroup(group_id)
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

-- indexes
CREATE INDEX idx_schedule_teacher ON Schedule(teacher_id);
CREATE INDEX idx_schedule_room ON Schedule(room_id);
CREATE INDEX idx_schedule_timeslot ON Schedule(time_slot_id);
CREATE INDEX idx_schedule_discipline ON Schedule(discipline_id);
CREATE INDEX idx_group_study_year ON FacultyGroup(study_year_id);
CREATE INDEX idx_room_type ON Room(room_type_id);
