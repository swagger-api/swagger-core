package com.wordnik.swagger.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ayush
 * @since 6/23/11 12:25 PM
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiOperation {

    String value();

    String responseClass() default "ok";

    boolean mutiValueResponse() default false; //to indicate if return type will contain one or more of the response value

    String notes() default "";

    String tags() default "";

}
