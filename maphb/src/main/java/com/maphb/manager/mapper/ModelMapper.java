package com.maphb.manager.mapper;

import com.maphb.annotations.Column;
import com.maphb.annotations.RowKey;
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

/**
 * The ModelMapper class is responsible for mapping objects to HBase Put objects,
 * retrieving row keys from objects, and creating objects from HBase Result objects.
 * It uses reflection to scan the fields of the object's class and perform the necessary operations.
 *
 * @param <T> the type of object to be mapped
 */
public class ModelMapper<T> {

    private final Class<T> classType;

    public ModelMapper(Class<T> clazz) {
        this.classType = clazz;
    }

    /**
        * Creates a Put object from the given row key and object.
        *
        * @param rowKey the row key for the Put object
        * @param object the object from which the Put object is created
        * @return the created Put object
        */
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


    /**
     * Retrieves the row key for the given object.
     *
     * @param object the object for which to retrieve the row key
     * @return the row key as a string
     */
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

    /**
     * Creates an object of type T from a Result object.
     *
     * @param result the Result object containing the data to create the object from
     * @return an object of type T created from the Result object
     */
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

    /**
     * Retrieves the value from the given field in the specified result.
     *
     * @param field  the field to retrieve the value from
     * @param result the result containing the value
     * @return the retrieved value as a string, or null if the field does not match the class type
     */
    private String retrieveValue(Field field, Result result) {
        String resultString = null;
        if (classType.getName().equalsIgnoreCase(field.getDeclaringClass().getName())) {
            String columnFamily = field.getAnnotation(Column.class).family();
            String qualifier = field.getAnnotation(Column.class).qualifier();

            resultString = Bytes.toString(result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier)));
        }
        return resultString;
    }

    /**
        * Retrieves the setter method for a given field.
        *
        * @param field the field for which to retrieve the setter method
        * @return the setter method for the field, or null if not found
        */
    private Method getSetter(Field field) {
        Method method = null;
        try {
            method = this.classType.getDeclaredMethod("set" + StringUtils.capitalize(field.getName()), String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    /**
        * Retrieves the getter method for a given field.
        *
        * @param field the field for which to retrieve the getter method
        * @return the getter method for the field, or null if not found
        */
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
