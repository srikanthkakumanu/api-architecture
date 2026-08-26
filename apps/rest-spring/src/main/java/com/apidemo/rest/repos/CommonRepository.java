package com.apidemo.rest.repos;

import java.util.Collection;

/**
 * Data or DB repository interface contract
 */
public interface CommonRepository<T> {
    public T save(T model);
    public Iterable<T> save(Collection<T> models);
    public void delete(T model);
    public T findById(String id);
    public Iterable<T> findAll();
}
