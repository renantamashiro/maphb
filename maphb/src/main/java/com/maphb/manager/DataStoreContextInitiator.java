package com.maphb.manager;

import com.maphb.annotations.Table;
import com.maphb.models.TableMetadata;
import org.apache.hadoop.hbase.client.Connection;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The DataStoreContextInitiator class is responsible for creating a context for interacting with the data store.
 */
public final class DataStoreContextInitiator {

    final static Logger log = LoggerFactory.getLogger(DataStoreContextInitiator.class);

    /**
     * Represents a context for interacting with the data store.
     */
    public static DataStoreContext create(Class<?> applicationContext, Connection connection) {
        Package mainPackage = applicationContext.getPackage();
        Reflections reflections = new Reflections(mainPackage.getName(), Scanners.values());

        Set<TableMetadata> tableMetadataSet = reflections.getTypesAnnotatedWith(Table.class)
                .stream()
                .map(clazz -> TableMetadata.builder()
                        .modelName(clazz.getName())
                        .tableName(clazz.getDeclaredAnnotation(Table.class).name())
                        .columnFamilies(Arrays.asList(clazz.getDeclaredAnnotation(Table.class).columnFamilies()))
                        .build())
                .collect(Collectors.toSet());

        for (TableMetadata tableMetadata : tableMetadataSet) {
            log.info("Table named as {} for model {} detected.", tableMetadata.getTableNameAsString(), tableMetadata.getModelName());
        }
        return new DataStoreContextImpl(connection, tableMetadataSet);
    }
}
