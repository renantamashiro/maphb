package maphb.manager;

import maphb.models.TableMetadata;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

class DataStoreContextImpl implements DataStoreContext {

    final static Logger log = LoggerFactory.getLogger(DataStoreContextImpl.class);

    private Connection connection;
    private Set<TableMetadata> tableMetadataSet;

    protected DataStoreContextImpl(Connection connection, Set<TableMetadata> tableMetadataSet) {
        this.connection = connection;
        this.tableMetadataSet = tableMetadataSet;

        if (tableMetadataSet.isEmpty()) {
            log.info("Any table will be created. No models are available.");
        } else {
            this.verify();
        }
    }

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

    private void create(TableMetadata tableMetadata, Admin admin) throws IOException {
        boolean notContainsNamespace = !Arrays.asList(admin.listNamespaces()).contains(tableMetadata.getNamespace());
        if (notContainsNamespace && Objects.nonNull(tableMetadata.getNamespace())) {
            admin.createNamespace(tableMetadata.getNamespaceDescriptor());
            log.info("Namespace {} created.", tableMetadata.getNamespace());
        }
        admin.createTable(tableMetadata.getTableDescriptor());
        log.info("Table {} created.", tableMetadata.getTableNameAsString());
    }

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
