package com.maphb.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that represents a HBase rowkey.
 * RowKey.class is used as an identifier for rowkey creation based on keys defined in it and a delimiter.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RowKey {

    /**
     * Entity fields that will be used as keys.
     */
    String[] keys();

    /**
     * Delimiter for keys separation.
     */
    String delimiter();

}
