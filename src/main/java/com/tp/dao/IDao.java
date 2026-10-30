package com.tp.dao;

import java.util.List;

/**
 * Interface générique DAO définissant les opérations CRUD standards.
 *
 * @param <T> Le type de l'entité JPA gérée.
 */
public interface IDao<T> {

    boolean create(T o);

    boolean update(T o);

    boolean delete(T o);

    T findById(Long id);

    default T findById(int id) {
        return findById(Long.valueOf(id));
    }

    List<T> findAll();
}
