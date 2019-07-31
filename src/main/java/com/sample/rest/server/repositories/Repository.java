package com.sample.rest.server.repositories;

import java.util.List;

public interface Repository<T, ID> {

    T find(ID id);

    List<T> findAll();

    T store(T entity);

}
