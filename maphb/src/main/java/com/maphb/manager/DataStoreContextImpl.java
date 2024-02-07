package com.maphb.manager;

import com.maphb.models.TableMetadata;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Implementation of the DataStoreContext interface.
 * This class manages the connection to the data store and performs table creation and modification based on the provided table metadata.
 */
class DataStoreContextImpl implements DataStoreContext {

    final static Logger log = LoggerFactory.getLogger(DataStoreContextImpl.class);

    private final Connection connection;
    private final Set<TableMetadata> tableMetadataSet;

    /**
     * Represents a context for managing data stores.
     */
    protected DataStoreContextImpl(Connection connection, Set<TableMetadata> tableMetadataSet) {
        this.connection = connection;
        this.tableMetadataSet = tableMetadataSet;

        if (tableMetadataSet.isEmpty()) {
            log.info("Any table will be created. No models are available.");
        } else {
            this.verify();
        }
    }

    /**
     * Verifies the existence of tables in HBase and performs necessary modifications or creations.
     */
    private void verify() {
        try (Admin admin = this.connection.getAdmin()) {
            for (TableMetadata tableMetadata : this.tableMetadataSet) {
                if (admin.tableExists(tableMetadata.getTableName())) {
                    modify(tableMetadata, admin);
                } else {
                    create(tableMetadata, admin);
                }
            }
        } catch (IOException e) {
            log.error("An error occurred during hbase table creation ()", e);
        }
    }

    /**
     * Creates a table in the HBase cluster based on the provided table metadata.
     * If the namespace specified in the table metadata does not exist, it will be created.
     *
     * @param tableMetadata The metadata of the table to be created.
     * @param admin         The HBase admin object used to interact with the HBase cluster.
     * @throws IOException If an error occurs while creating the table or namespace.
     */
    private void create(TableMetadata tableMetadata, Admin admin) throws IOException {
        boolean notContainsNamespace = !Arrays.asList(admin.listNamespaces()).contains(tableMetadata.getNamespace());
        if (notContainsNamespace && Objects.nonNull(tableMetadata.getNamespace())) {
            admin.createNamespace(tableMetadata.getNamespaceDescriptor());
            log.info("Namespace {} created.", tableMetadata.getNamespace());
        }
        admin.createTable(tableMetadata.getTableDescriptor());
        log.info("Table {} created.", tableMetadata.getTableNameAsString());
    }

    /**
     * Modifies the table based on the provided table metadata and admin object.
     * If there are new column families in the table metadata, the table is modified accordingly.
     *
     * @param tableMetadata The metadata of the table to be modified.
     * @param admin         The admin object used to interact with the HBase cluster.
     * @throws IOException If an error occurs while modifying the table.
     */
    private void modify(TableMetadata tableMetadata, Admin admin) throws IOException {
        Optional<TableDescriptor> optionalTableDescriptor = admin.listTableDescriptors().stream()
                .filter(tableDescriptor -> tableDescriptor.getTableName()
                        .equals(tableMetadata.getTableName()))
                .findFirst();

        if (optionalTableDescriptor.isPresent()) {
            List<String> currentColumnFamilies = Arrays.stream(optionalTableDescriptor.get().getColumnFamilies())
                    .map(ColumnFamilyDescriptor::getNameAsString)
                    .toList();

            boolean hasNewColumnFamily = tableMetadata.getColumnFamilyDescriptors()
                    .stream()
                    .anyMatch(columnFamily -> !currentColumnFamilies.contains(columnFamily.getNameAsString()));

            if (hasNewColumnFamily) {
                log.info("Changes detected for {} table.", tableMetadata.getTableNameAsString());
                admin.modifyTable(tableMetadata.getTableDescriptor());
                log.info("Table {} modified", tableMetadata.getTableNameAsString());
            }
        }
    }

    @Override
    public Set<TableMetadata> getTableMetadataSet() {
        return this.tableMetadataSet;
    }
}
