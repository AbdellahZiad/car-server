package com.suprateam.car.service.impl.export.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface RowCell {
    String label() default "";
    String description() default "";
    int index() default -1;
}
