package maphb.manager;

import maphb.models.TableMetadata;

import java.util.Set;

public interface DataStoreContext {

    Set<TableMetadata> getTableMetadataSet();

}
