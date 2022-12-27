package com.maphb.manager.mapper;

import com.maphb.annotations.Column;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModelMapper<T> {

    private Class<T> classType;

    public ModelMapper(Class<T> clazz) {
        this.classType = clazz;
    }

    public T create(Result result) {
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
}