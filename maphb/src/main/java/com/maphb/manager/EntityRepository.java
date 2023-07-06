package com.maphb.manager;

import com.maphb.models.TableFilter;

import java.util.List;

public interface EntityRepository<T> {

    T get(String rowKey);

    T put(T object);

    List<T> put(List<T> listOfObjects);

    void delete(String rowKey);

    List<T> scan(TableFilter<T> filterList);

}
