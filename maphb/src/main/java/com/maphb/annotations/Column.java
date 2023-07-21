package com.maphb.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that represents a Column qualifier HBase. Field annotated with Column.class will be interpreted as a
 * qualifier.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * HBase column family string
     */
    String family();

    /**
     * HBase column qualifier string
     */
    String qualifier();
}
