package io.swagger.v3.oas.annotations.links;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a parameter to pass to an operation as specified with operationId or identified via operationRef.
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LinkParameter {
    /**
     * The name of this link parameter.
     *
     * @return the parameter's name
     **/
    String name() default "";

    /**
     * A constant or an expression to be evaluated and passed to the linked operation.
     *
     * @return the parameter's value
     **/
    String expression() default "";

}
