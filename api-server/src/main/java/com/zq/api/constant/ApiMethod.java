package com.zq.api.constant;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wilmiam
 * @since 2022/1/5 10:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiMethod {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    String service() default "";

}
