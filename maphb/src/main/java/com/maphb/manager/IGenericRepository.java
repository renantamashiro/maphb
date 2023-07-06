package com.maphb.manager;

import com.maphb.models.TableFilter;

import java.util.List;

public interface IGenericRepository<T> {

    T get(String rowkey);

    T put(T object);

    List<T> put(List<T> listOfObjects);

    void delete(String rowkey);

    List<T> scan(TableFilter<T> tableFilter);

}
