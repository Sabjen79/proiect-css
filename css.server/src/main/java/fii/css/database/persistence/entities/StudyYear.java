package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("StudyYear")
public class StudyYear extends DatabaseEntity {
    @Id
    @Column("study_year_id")
    private String studyYearId;

    @Column("degree")
    private int degree;

    @Column("specialty")
    private String specialty;

    @Column("max_years")
    private int maxYears;

    public StudyYear() {}

    public String getId() {
        return studyYearId;
    }

    public Degree getDegree() {
        return Degree.fromValue(degree);
    }

    public void setDegree(Degree degree) {
        this.degree = degree.value;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getMaxYears() {
        return maxYears;
    }

    public void setMaxYears(int maxYears) {
        this.maxYears = maxYears;
    }

    @Override
    public DatabaseEntity clone() {
        var studyYear = new StudyYear();

        studyYear.studyYearId = this.studyYearId;
        studyYear.degree = this.degree;
        studyYear.specialty = this.specialty;
        studyYear.maxYears = this.maxYears;

        return studyYear;
    }
}
