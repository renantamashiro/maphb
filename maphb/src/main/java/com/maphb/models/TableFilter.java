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

public class TableFilter<T> {

    private FilterList filterList;

    private byte[] prefixFilter;

    private final Class<T> modelClass;

    public TableFilter(Class<T> clazz) {
        this.filterList = new FilterList();
        this.modelClass = clazz;
    }

    public byte[] getPrefixFilter() {
        return this.prefixFilter;
    }

    public boolean hasPrefixFilter() {
        return Objects.nonNull(this.prefixFilter);
    }

    public FilterList getFilterList() {
        return this.filterList;
    }

    public void setRowKeyPrefixFilter(String prefixFilter) {
        this.prefixFilter = Bytes.toBytes(prefixFilter);
    }

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
