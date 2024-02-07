package com.maphb.manager;

import com.maphb.models.TableFilter;

import java.util.List;

/**
 * This interface represents a generic repository for managing entities.
 *
 * @param <T> the type of entity managed by the repository
 */
public interface EntityRepository<T> {

    /**
     * Retrieves an entity by its row key.
     *
     * @param rowKey the row key of the entity
     * @return the retrieved entity, or null if not found
     */
    T get(String rowKey);

    /**
     * Stores an entity in the repository.
     *
     * @param object the entity to be stored
     * @return the stored entity
     */
    T put(T object);

    /**
     * Stores a list of entities in the repository.
     *
     * @param listOfObjects the list of entities to be stored
     * @return the list of stored entities
     */
    List<T> put(List<T> listOfObjects);

    /**
     * Deletes an entity from the repository by its row key.
     *
     * @param rowKey the row key of the entity to be deleted
     */
    void delete(String rowKey);

    /**
     * Scans the repository using the provided filter list.
     *
     * @param filterList the list of filters to be applied during the scan
     * @return the list of entities matching the filters
     */
    List<T> scan(TableFilter<T> filterList);

}
