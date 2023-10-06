package com.example.demoservice;


import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HBaseDemo {

    @Autowired
    private Connection connection;
    
    public void log(String... messages) {
        Arrays.asList(messages).forEach(System.out::println);
    }

    public String getValueInColumnQualifier() throws RowKeyNotFoundException {
        String value = null;

        Get getOperation = new Get(Bytes.toBytes("row-1"));
        try (Table table  = this.connection.getTable(TableName.valueOf("tb_hbase"))) {
            Result result = table.get(getOperation);

            if (result.isEmpty()) {
                throw new RowKeyNotFoundException();
            } else {
                value = Bytes.toString(result.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("cq1")));
            }
        } catch (IOException e) {
            log("HBase connection exception: ", e.getMessage());
        }
        return value;
    }

    public void putNewEntity() throws PutOperationFailedException {
        byte[] rowKey = Bytes.toBytes("row-100");

        Put putOperation = new Put(rowKey);
        putOperation.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("cq1"), Bytes.toBytes("fake_value1"));
        putOperation.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("cq2"), Bytes.toBytes("fake_value2"));
        putOperation.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("cq3"), Bytes.toBytes("fake_value3"));
        putOperation.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("cq4"), Bytes.toBytes("fake_value4"));

        try (Table table  = this.connection.getTable(TableName.valueOf("tb_hbase"))) {
            table.put(putOperation);

            if (!table.exists(new Get(rowKey))) {
                throw new PutOperationFailedException();
            }
        } catch (IOException e) {
            log("HBase connection exception: ", e.getMessage());
        }
    }

    public void deleteAnEntity() throws IncompleteDeleteOperationException {
        byte[] rowKey = Bytes.toBytes("row-100");

        Delete delete = new Delete(rowKey);

        try (Table table  = this.connection.getTable(TableName.valueOf("tb_hbase"))) {
            table.delete(delete);

            if (table.exists(new Get(rowKey))) {
                throw new IncompleteDeleteOperationException();
            }
        } catch (IOException e) {
            log("HBase connection exception: ", e.getMessage());
        }
    }

    public Map<String, String> scanTable() {
        Map<String, String> resultMap = new HashMap<>();

        Scan scan = new Scan();
        byte[] family = Bytes.toBytes("cf1");
        String qualifier1 = "cq1";
        String qualifier2 = "cq2";
        String qualifier3 = "cq3";
        String qualifier4 = "cq4";

        scan.setStartStopRowForPrefixScan(Bytes.toBytes("row"));
        scan.setCaching(1000);

        try (Table table  = this.connection.getTable(TableName.valueOf("tb_hbase"))) {
            ResultScanner resultScanner = table.getScanner(scan);

            Result result = resultScanner.next();
            while (result != null) {
                resultMap.put(qualifier1, Bytes.toString(result.getValue(family, Bytes.toBytes(qualifier1))));
                resultMap.put(qualifier2, Bytes.toString(result.getValue(family, Bytes.toBytes(qualifier2))));
                resultMap.put(qualifier3, Bytes.toString(result.getValue(family, Bytes.toBytes(qualifier3))));
                resultMap.put(qualifier4, Bytes.toString(result.getValue(family, Bytes.toBytes(qualifier4))));

                result = resultScanner.next();
            }
            resultScanner.close();
        } catch (IOException e) {
            log("HBase connection exception: ", e.getMessage());
        }

        return resultMap;
    }

}
