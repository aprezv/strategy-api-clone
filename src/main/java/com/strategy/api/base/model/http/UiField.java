package com.strategy.api.base.model.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 6/12/18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UiField {
    boolean required() default false;
    boolean showInGrid() default false;
    UiFieldType type() default UiFieldType.TEXT;
    double min() default 0;
    double max() default 0;
    String regex() default "";
    String path() default "";
    String displayName() default "";
    String searchAttribute() default "";
}
