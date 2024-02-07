package com.maphb.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to specify the HBase column family and qualifier for a field.
 * This annotation should be applied to fields in classes that are mapped to HBase tables.
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
