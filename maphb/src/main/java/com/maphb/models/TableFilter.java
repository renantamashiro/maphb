package com.maphb.models;

import com.maphb.annotations.Column;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Represents a filter for querying a table based on specific criteria.
 *
 * @param <T> the type of the model used for filtering
 */
public class TableFilter<T> {

    private FilterList filterList;

    private byte[] prefixFilter;

    private final Class<T> modelClass;

    /**
     * Constructs a new TableFilter object with the specified model class.
     *
     * @param clazz the class of the model used for filtering
     */
    public TableFilter(Class<T> clazz) {
        this.filterList = new FilterList();
        this.modelClass = clazz;
    }

    /**
     * Gets the prefix filter byte array.
     *
     * @return the prefix filter byte array
     */
    public byte[] getPrefixFilter() {
        return this.prefixFilter;
    }

    /**
     * Checks if a prefix filter is set.
     *
     * @return true if a prefix filter is set, false otherwise
     */
    public boolean hasPrefixFilter() {
        return Objects.nonNull(this.prefixFilter);
    }

    /**
     * Gets the filter list.
     *
     * @return the filter list
     */
    public FilterList getFilterList() {
        return this.filterList;
    }

    /**
     * Sets the row key prefix filter.
     *
     * @param prefixFilter the prefix filter value
     */
    public void setRowKeyPrefixFilter(String prefixFilter) {
        this.prefixFilter = Bytes.toBytes(prefixFilter);
    }

    /**
     * Sets the filter based on a model example.
     *
     * @param modelExample the model example used for filtering
     */
    public void setFilterByModelExample(T modelExample) {
        Object fieldValue;
        Reflections reflections = new Reflections(this.modelClass.getPackageName(), Scanners.values());
        for (Field field : reflections.getFieldsAnnotatedWith(Column.class)) {
            Method method = this.getGetter(field);

            try {
                fieldValue = method.invoke(modelExample);
            } catch (InvocationTargetException | IllegalAccessException e) {
                continue;
            }

            String family = field.getAnnotation(Column.class).family();
            String qualifier = field.getAnnotation(Column.class).qualifier();

            if (Objects.nonNull(fieldValue)) {
                SingleColumnValueFilter filter = new SingleColumnValueFilter(
                        Bytes.toBytes(family),
                        Bytes.toBytes(qualifier),
                        CompareOperator.EQUAL,
                        Bytes.toBytes(String.valueOf(fieldValue))
                );
                filter.setFilterIfMissing(true);
                this.filterList.addFilter(filter);
            }
        }
    }

    private Method getGetter(Field field) {
        Method method = null;
        try {
            method = this.modelClass.getDeclaredMethod("get" + StringUtils.capitalize(field.getName()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }
}
