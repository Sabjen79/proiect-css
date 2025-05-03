package fii.css.database.persistence.repositories;

import fii.css.database.persistence.entities.Discipline;
import fii.css.database.persistence.entities.Teacher;

public class DisciplineRepository extends AbstractRepository<Discipline> {
    public DisciplineRepository() {
        super(Discipline.class);
    }
}