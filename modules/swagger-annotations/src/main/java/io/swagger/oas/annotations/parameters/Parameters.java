package io.swagger.oas.annotations.parameters;

import io.swagger.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER,
        ElementType.METHOD })
@Inherited
public @interface Parameters {
    public Parameter[] parameters() default @Parameter;

    public Parameter[] value() default {@Parameter};
}
