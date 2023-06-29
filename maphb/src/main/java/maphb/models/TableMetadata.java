package maphb.models;

import maphb.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
public class TableMetadata {

    private String modelName;

    private String tableName;

    private List<String> columnFamilies;

    public TableMetadata() {

    }

    public TableMetadata(Class<?> clazz) {
        this.modelName = clazz.getName();
        this.tableName = clazz.getDeclaredAnnotation(Table.class).name();
        this.columnFamilies = Arrays.asList(clazz.getDeclaredAnnotation(Table.class).columnFamilies());

    }


    public String getModelName() {
        return this.modelName;
    }

    public String getTableNameAsString() {
        return this.tableName;
    }

    public String getNamespace() {
        String namespace = null;
        if (this.tableName.contains(":")) {
            namespace = this.tableName.split(":")[0];
        }
        return namespace;
    }

    public NamespaceDescriptor getNamespaceDescriptor() {
        NamespaceDescriptor namespaceDescriptor = null;
        if (Objects.nonNull(this.getNamespace())) {
            namespaceDescriptor = NamespaceDescriptor.create(this.getNamespace()).build();
        }
        return namespaceDescriptor;
    }

    public TableName getTableName() {
        return TableName.valueOf(this.tableName);
    }

    public List<String> getColumnFamiliesAsString() {
        return this.columnFamilies;
    }

    public List<byte[]> getColumnFamilies() {
        return this.columnFamilies.stream().map(Bytes::toBytes).collect(Collectors.toList());
    }

    public TableDescriptor getTableDescriptor() {
        return TableDescriptorBuilder.newBuilder(this.getTableName())
                .setColumnFamilies(this.getColumnFamilyDescriptors())
                .build();
    }

    public List<ColumnFamilyDescriptor> getColumnFamilyDescriptors() {
        return this.getColumnFamilies().stream().map(cf ->
                ColumnFamilyDescriptorBuilder.newBuilder(cf).build()
        ).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return this.tableName + " for model " + this.modelName;
    }
}
