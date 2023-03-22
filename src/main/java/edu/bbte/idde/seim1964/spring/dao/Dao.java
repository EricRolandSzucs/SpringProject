package edu.bbte.idde.seim1964.spring.dao;

import edu.bbte.idde.seim1964.spring.model.BaseEntity;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T extends BaseEntity> {
    Collection<T> findAll();

    T saveAndFlush(T entity);

    void deleteById(Long id);

    Optional<T> findById(Long id);
}

