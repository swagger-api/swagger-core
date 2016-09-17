package io.swagger.annotations;

/**
 * Annotation used to construct Basic Auth security definition.
 */
public @interface BasicAuthDefinition {

    /**
     * Key used to refer to this security definition
     *
     * @return key used to refer to this security definition
     */
    String key();

    /**
     * A short description for security scheme.
     *
     * @return a short description for security scheme.
     */
    String description() default "";
}
