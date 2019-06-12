package com.nataliia.jbdc;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {

    boolean save(T t);

    Optional<T> get(ID id);

    Optional<T> update(T t);

    void delete(ID id);

    List<T> getAll();
}
