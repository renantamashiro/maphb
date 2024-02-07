package com.maphb.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation used to specify the row key for a field in a data model.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RowKey {

    /**
     * The keys that make up the row key.
     *
     * @return an array of strings representing the keys
     */
    String[] keys();

    /**
     * The delimiter used to separate the keys in the row key.
     *
     * @return the delimiter string
     */
    String delimiter();

}
