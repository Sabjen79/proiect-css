package fii.css.database.persistence.entities;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("Discipline")
public class Discipline extends DatabaseEntity {
    @Id
    @Column("discipline_id")
    private String disciplineId;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("year")
    private int year;

    @Column("study_year_id")
    private String studyYearId;

    public Discipline() {}

    public String getId() {
        return disciplineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStudyYearId() {
        return studyYearId;
    }

    public void setStudyYearId(String studyYearId) {
        this.studyYearId = studyYearId;
    }
}
