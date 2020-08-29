package com.suprateam.car.service.impl.export.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface RowIndex {
    int value() default -1;
}
