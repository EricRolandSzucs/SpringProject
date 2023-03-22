package edu.bbte.idde.seim1964.backend.dao;

import edu.bbte.idde.seim1964.backend.model.BaseEntity;

import java.util.Collection;

public interface Dao<T extends BaseEntity> {
    Collection<T> findAll();

    T create(T entity);

    void delete(Long id);

    T update(Long id, T entity);

    T findById(Long id);
}

