package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("StudyYear")
public class StudyYear extends DatabaseEntity {
    @Id
    @Column("stu")
    private int studyYearId;
    private String value;

    public StudyYear(int studyYearId, String value) {
        this.studyYearId = studyYearId;
        this.value = value;
    }

    public int getStudyYearId() {
        return studyYearId;
    }

    public void setStudyYearId(int studyYearId) {
        this.studyYearId = studyYearId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
