package com.maphb.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation responsible for declare a HBase Table.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * HBase table name. (Consider the full name: namespace + table_name)
     */
    String name();

    /**
     * Table column families.
     */
    String[] columnFamilies();
}
