package com.maphb.manager;

import com.maphb.models.TableMetadata;

import java.util.Set;

/**
 * This interface represents a data store context.
 * It provides methods to retrieve table metadata.
 */
public interface DataStoreContext {

    /**
     * Retrieves the set of table metadata.
     *
     * @return the set of table metadata
     */
    Set<TableMetadata> getTableMetadataSet();

}
