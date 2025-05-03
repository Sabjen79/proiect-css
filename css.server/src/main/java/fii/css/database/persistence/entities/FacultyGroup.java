package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("FacultyGroup")
public class FacultyGroup extends DatabaseEntity {
    @Id
    @Column("faculty_group_id")
    private String facultyGroupId;

    @Column("name")
    private String groupName;

    @Column("year")
    private int year;

    @Column("study_year_id")
    private String studyYearId;

    public FacultyGroup() {}

    public String getId() {
        return facultyGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setName(String groupName) {
        this.groupName = groupName;
    }

    public StudyYear getStudyYear() {
        return Database
            .getInstance()
            .studyYearManager
            .get(studyYearId);
    }

    public void setStudyYearId(String id) {
        studyYearId = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public DatabaseEntity clone() {
        var facultyGroup = new FacultyGroup();

        facultyGroup.facultyGroupId = facultyGroupId;
        facultyGroup.groupName = groupName;
        facultyGroup.year = year;
        facultyGroup.studyYearId = studyYearId;

        return facultyGroup;
    }
}
