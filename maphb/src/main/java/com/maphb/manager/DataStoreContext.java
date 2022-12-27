package com.maphb.manager;

import com.maphb.models.TableMetadata;

import java.util.Set;

public interface DataStoreContext {

    Set<TableMetadata> getTableMetadataSet();

}
