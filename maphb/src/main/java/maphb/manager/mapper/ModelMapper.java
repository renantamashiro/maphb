package maphb.manager.mapper;

import maphb.annotations.Column;
import maphb.annotations.RowKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModelMapper<T> {

    private final Class<T> classType;

    public ModelMapper(Class<T> clazz) {
        this.classType = clazz;
    }

    public Put createPutFromObject(String rowKey, T object) {
        Put putObject = new Put(Bytes.toBytes(rowKey));
        Reflections reflections = new Reflections(classType.getPackageName(), Scanners.values());
        for (Field field : reflections.getFieldsAnnotatedWith(Column.class)) {
            byte[] fieldValue;
            try {
                Method method = getGetter(field);
                fieldValue = Bytes.toBytes(String.valueOf(method.invoke(object)));
            } catch (InvocationTargetException | IllegalAccessException e) {
                fieldValue = new byte[]{};
            }

            putObject.addColumn(
                    Bytes.toBytes(field.getAnnotation(Column.class).family()),
                    Bytes.toBytes(field.getAnnotation(Column.class).qualifier()),
                    fieldValue
            );
        }

        return putObject;
    }

    public String getRowKey(T object) {
        StringBuilder rowKey = new StringBuilder();

        String[] keys = new String[]{};
        String delimiter = "";

        Reflections reflections = new Reflections(classType.getPackageName(), Scanners.values());
        for (Field field : reflections.getFieldsAnnotatedWith(RowKey.class)) {
            keys = field.getAnnotation(RowKey.class).keys();
            delimiter = field.getAnnotation(RowKey.class).delimiter();
        }

        for (String fieldName : keys) {
            try {
                Method method = getGetter(classType.getDeclaredField(fieldName));

                rowKey.append(method.invoke(object));
                rowKey.append(delimiter);

            } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        rowKey.deleteCharAt(rowKey.length() - 1);

        return rowKey.toString();
    }

    public T createObjectFromResult(Result result) {
        T object;
        try {
            Constructor<T> constructor = classType.getConstructor();
            object = constructor.newInstance();

            Reflections r = new Reflections(classType.getPackageName(), Scanners.values());
            r.getFieldsAnnotatedWith(Column.class).forEach(field -> {
                if (classType.getName().equalsIgnoreCase(field.getDeclaringClass().getName())) {
                    Method method = getSetter(field);
                    try {
                        method.invoke(object, retrieveValue(field, result));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return object;
    }

    private String retrieveValue(Field field, Result result) {
        String resultString = null;
        if (classType.getName().equalsIgnoreCase(field.getDeclaringClass().getName())) {
            String columnFamily = field.getAnnotation(Column.class).family();
            String qualifier = field.getAnnotation(Column.class).qualifier();

            resultString = Bytes.toString(result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier)));
        }
        return resultString;
    }

    private Method getSetter(Field field) {
        Method method = null;
        try {
            method = this.classType.getDeclaredMethod("set" + StringUtils.capitalize(field.getName()), String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    private Method getGetter(Field field) {
        Method method = null;
        try {
            method = this.classType.getDeclaredMethod("get" + StringUtils.capitalize(field.getName()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }
}
