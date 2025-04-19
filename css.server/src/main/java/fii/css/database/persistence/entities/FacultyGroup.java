package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("FacultyGroup")
public class FacultyGroup extends DatabaseEntity {
    @Id
    @Column("faculty_group_id")
    private int facultyGroupId;

    @Column("name")
    private String facultyGroupName;

    @Column("study_year_id")
    private int studyYearId;

    public int getFacultyGroupId() {
        return facultyGroupId;
    }

    public String getFacultyGroupName() {
        return facultyGroupName;
    }

    public void setFacultyGroupName(String facultyGroupName) {
        this.facultyGroupName = facultyGroupName;
    }

    public int getStudyYearId() {
        return studyYearId;
    }

    public void setStudyYearId(int studyYearId) {
        this.studyYearId = studyYearId;
    }
}
