package fii.css.database.domain;

public class StudyYear {
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
