package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

import java.util.List;

@Table("SemiYear")
public class SemiYear extends DatabaseEntity {
    @Id
    @Column("id")
    private String id;

    @Column("name")
    private String name;

    @Column("degree")
    private int degree;

    @Column("year")
    private int year;

    public SemiYear() {}

    public SemiYear(String id, String name, int degree, int year) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Degree getDegree() {
        assert degree > -1 && degree < Degree.values().length;

        return Degree.fromValue(degree);
    }

    public void setDegree(Degree degree) {
        this.degree = degree.value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<FacultyGroup> getFacultyGroups() {
        return Database.getInstance().getFacultyGroupManager()
                .getAll()
                .stream()
                .filter(fg -> fg.getSemiYearId().equals(id))
                .toList();
    }

    @Override
    public DatabaseEntity clone() {
        var studyYear = new SemiYear();

        studyYear.id = this.id;
        studyYear.name = this.name;
        studyYear.degree = this.degree;
        studyYear.year = this.year;

        return studyYear;
    }
}
