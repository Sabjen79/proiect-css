package fii.css.database.persistence.entities;

import fii.css.database.Database;
import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.annotations.Column;
import fii.css.database.persistence.annotations.Id;
import fii.css.database.persistence.annotations.Table;

@Table("FacultyGroup")
public class FacultyGroup extends DatabaseEntity {
    @Id
    @Column("id")
    private String id;

    @Column("name")
    private String name;

    @Column("semi_year_id")
    private String semiYearId;

    public FacultyGroup() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String groupName) {
        this.name = groupName;
    }

    public String getSemiYearId() {
        return semiYearId;
    }

    public void setSemiYearId(String id) {
        semiYearId = id;
    }

    @Override
    public DatabaseEntity clone() {
        var facultyGroup = new FacultyGroup();

        facultyGroup.id = this.id;
        facultyGroup.name = this.name;
        facultyGroup.semiYearId = this.semiYearId;

        return facultyGroup;
    }
}
