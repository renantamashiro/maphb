package com.maphb.manager;

import com.maphb.manager.mapper.ModelMapper;
import com.maphb.models.TableMetadata;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

class EntityRepositoryImpl<T> implements EntityRepository<T> {

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
    public T get(String rowkey) {
        T finalObject = null;
        try (Table table = this.connection.getTable(this.tableMetadata.getTableName())) {
            Result result = table.get(new Get(Bytes.toBytes(rowkey)));
            if (result != null) {
                finalObject = this.modelMapper.create(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalObject;
    }

    @Override
    public void put(Object object) {

    }

    @Override
    public void put(List<T> listOfObjects) {

    }

    @Override
    public void delete(String rowkey) {

    }

    @Override
    public List<T> scan() {
        return null;
    }
}
