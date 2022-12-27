package com.maphb.manager;

import java.util.List;

public interface IGenericRepository<T> {

    T get(String rowkey);

    void put(T object);

    void put(List<T> listOfObjects);

    void delete(String rowkey);

    List<T> scan();

}
