package io.swagger.v3.oas.annotations.servers;

import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An object representing a Server Variable for server URL template substitution.
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ServerVariable {
    /**
     * Required.  The name of this variable.
     *
     * @return String name
     **/
    String name();

    /**
     * An array of allowable values for this variable.  This field map to the enum property in the OAS schema.
     *
     * @return String array of allowableValues
     **/
    String[] allowableValues() default "";

    /**
     * Required.  The default value of this variable.
     *
     * @return String defaultValue
     **/
    String defaultValue();

    /**
     * An optional description for the server variable.
     *
     * @return String description
     **/
    String description() default "";

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

}
