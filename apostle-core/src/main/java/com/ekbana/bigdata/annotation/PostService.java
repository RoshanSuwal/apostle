package com.ekbana.bigdata.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface PostService {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}
