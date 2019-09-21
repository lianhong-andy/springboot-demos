package com.andy.orm.jdbctemplate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 所有阶段都保留
 */
@Retention(RetentionPolicy.RUNTIME)
/**作用在字段上*/
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * 列名
     *
     * @return 列名
     */
    String name();
}
