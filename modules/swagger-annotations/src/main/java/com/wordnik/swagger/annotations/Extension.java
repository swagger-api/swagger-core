package com.wordnik.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An optionally named list of extension properties.
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Extension {

    /**
     * @return an option name for these extensions - will be prefixed with "x-"
     */

    String name() default "";

    /**
     * @return the actual extension properties
     */

    ExtensionProperty[] properties();
}
