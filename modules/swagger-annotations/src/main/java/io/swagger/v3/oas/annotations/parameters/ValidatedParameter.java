package io.swagger.v3.oas.annotations.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Variant of JSR-303's jakarta.validation.Valid, supporting the
 * specification of validation groups.
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatedParameter {
    Class<?>[] value() default {};
}
