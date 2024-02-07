package com.maphb.manager;

import com.maphb.models.TableFilter;

import java.util.List;

/**
 * This interface represents a generic repository for managing objects of type T.
 */
public interface IGenericRepository<T> {

    /**
     * Retrieves an object of type T based on the given row key.
     *
     * @param rowkey the row key used to retrieve the object
     * @return the retrieved object of type T
     */
    T get(String rowkey);

    /**
     * Inserts or updates the given object of type T.
     *
     * @param object the object to be inserted or updated
     * @return the inserted or updated object of type T
     */
    T put(T object);

    /**
     * Inserts or updates a list of objects of type T.
     *
     * @param listOfObjects the list of objects to be inserted or updated
     * @return the inserted or updated list of objects of type T
     */
    List<T> put(List<T> listOfObjects);

    /**
     * Deletes an object of type T based on the given row key.
     *
     * @param rowkey the row key used to delete the object
     */
    void delete(String rowkey);

    /**
     * Scans and retrieves a list of objects of type T based on the given table filter.
     *
     * @param tableFilter the table filter used to scan the objects
     * @return the list of scanned objects of type T
     */
    List<T> scan(TableFilter<T> tableFilter);

}
