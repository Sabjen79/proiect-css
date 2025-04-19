package fii.css.database.persistence.managers;

import fii.css.database.persistence.entities.TeacherDiscipline;
import fii.css.database.persistence.repositories.TeacherDisciplineRepository;

public class TeacherDisciplineManager extends AbstractEntityManager<TeacherDiscipline> {
    public TeacherDisciplineManager() {super(new TeacherDisciplineRepository());}

}
