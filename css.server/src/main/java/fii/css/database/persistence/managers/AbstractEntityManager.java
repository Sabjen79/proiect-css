package fii.css.database.persistence.managers;

import fii.css.database.persistence.DatabaseEntity;
import fii.css.database.persistence.repositories.AbstractRepository;

import java.util.List;

public abstract class AbstractEntityManager<T extends DatabaseEntity> {
    protected final AbstractRepository<T> repository;

    public AbstractEntityManager(AbstractRepository<T> repo) {
        this.repository = repo;
    }

    abstract public T get(String id);
    abstract public List<T> getAll();
    abstract public void remove(String id);
}
