package com.ekbana.bigdata.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Policy
public @interface Validator {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}
