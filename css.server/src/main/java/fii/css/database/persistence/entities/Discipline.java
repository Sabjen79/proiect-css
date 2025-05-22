package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

import java.util.UUID;

@Table("Discipline")
public class Discipline extends DatabaseEntity {
    @Id
    @Column("id")
    private String id;

    @Column("name")
    private String name;

    @Column("degree")
    private int degree;

    @Column("year")
    private int year;

    public Discipline() {}

    public Discipline(String id, String name, int degree, int year) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        id = UUID.randomUUID().toString();
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

    @Override
    public DatabaseEntity clone() {
        var discipline = new Discipline();

        discipline.id = id;
        discipline.name = name;
        discipline.degree = degree;
        discipline.year = year;

        return discipline;
    }
}
