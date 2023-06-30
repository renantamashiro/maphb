package com.maphb.manager;

import com.maphb.manager.mapper.ModelMapper;
import com.maphb.models.TableMetadata;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

class EntityRepositoryImpl<T> implements EntityRepository<T> {

    final static Logger log = LoggerFactory.getLogger(EntityRepositoryImpl.class);
    final static int CACHING = 1000;
    final static String DEFAULT_ERROR_MESSAGE = "An error occurred during HBase connection ()";

    private final Class<T> classType;
    private final ModelMapper<T> modelMapper;
    private final Connection connection;

    private final TableMetadata tableMetadata;

    protected EntityRepositoryImpl(Connection connection, Class<T> clazz) {
        this.classType = clazz;
        this.connection = connection;
        this.tableMetadata = new TableMetadata(clazz);
        this.modelMapper = new ModelMapper<>(clazz);
    }

    @Override
    public T get(String rowKey) {
        T finalObject = null;
        try (Table table = this.connection.getTable(this.tableMetadata.getTableName())) {
            Result result = table.get(new Get(Bytes.toBytes(rowKey)));
            if (!result.isEmpty()) {
                log.info("Object found for given {} row key.", rowKey);
                finalObject = this.modelMapper.createObjectFromResult(result);
            } else {
                log.info("Object not found for given {} row key. Returning null object", rowKey);
            }
        } catch (IOException e) {
            log.error(DEFAULT_ERROR_MESSAGE, e);
        }

        return finalObject;
    }

    @Override
    public T put(T object) {
        T newObject = null;

        try (Table table = this.connection.getTable(this.tableMetadata.getTableName())) {
            String rowKey = modelMapper.getRowKey(object);
            log.info("Rowkey created {}", rowKey);
            table.put(modelMapper.createPutFromObject(rowKey, object));

            if (table.exists(new Get(Bytes.toBytes(rowKey)))) {
                Constructor<T> instanceObject = classType.getConstructor();
                newObject = instanceObject.newInstance();

                newObject = this.get(rowKey);
                log.info("Object created in {} table.", tableMetadata.getTableName());
            }

        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 IOException e) {
            log.error(DEFAULT_ERROR_MESSAGE, e);
        }

        return newObject;
    }

    @Override
    public List<T> put(List<T> listOfObjects) {
        List<T> listOfObjectsCreated = new ArrayList<>(listOfObjects.size());

        for (T object : listOfObjects) {
            listOfObjectsCreated.add(this.put(object));
        }

        return listOfObjectsCreated;
    }

    @Override
    public void delete(String rowKey) {
        try (Table table = this.connection.getTable(this.tableMetadata.getTableName())) {
            table.delete(new Delete(Bytes.toBytes(rowKey)));
        } catch (IOException e) {
            log.error(DEFAULT_ERROR_MESSAGE, e);
        }
    }

    @Override
    public List<T> scan(FilterList filterList) {
        List<T> objectsList = new ArrayList<>();

        Scan scan = new Scan();
        scan.setFilter(filterList);
        scan.setCaching(CACHING);

        try (Table table = this.connection.getTable(this.tableMetadata.getTableName())) {
            ResultScanner scanner = table.getScanner(scan);

            log.info(scanner.toString());
            for (Result result : scanner) {
                if (result != null) {
                    log.info("Scan result {}", result);
                    objectsList.add(this.modelMapper.createObjectFromResult(result));
                }
            }

            scanner.close();
        } catch (IOException e) {
            log.error(DEFAULT_ERROR_MESSAGE, e);
        }

        return objectsList;
    }
}
