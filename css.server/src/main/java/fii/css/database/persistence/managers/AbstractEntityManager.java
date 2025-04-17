package fii.css.database.persistence.managers;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.repositories.AbstractRepository;

public class AbstractEntityManager<T extends DatabaseEntity> {
    public final AbstractRepository<T> repository;

    public AbstractEntityManager(AbstractRepository<T> repo) {
        this.repository = repo;
    }
}
