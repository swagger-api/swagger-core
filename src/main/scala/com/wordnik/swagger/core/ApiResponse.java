package com.wordnik.swagger.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ayush
 * @since 6/23/11 12:27 PM
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResponse {

    String value() default "ok";  //responseClass

    String occurs() default "1"; //to indicate if return type will contain one or more of the response value

    ApiError[] errors() default @ApiError(code = 404, reason = "No data available");
}
