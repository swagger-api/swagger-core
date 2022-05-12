package io.swagger.v3.oas.annotations.media;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation may be used in {@link Schema#discriminatorMapping()} to define an optional mapping definition
 * in scenarios involving composition / inheritance where the value of the discriminator field does not match the schema
 * name or implicit mapping is not possible.
 *
 * <p>Use {@link Schema#discriminatorProperty()} to define a discriminator property.</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#discriminatorObject">Discriminator (OpenAPI specification)</a>
 * @see Schema
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiscriminatorMapping {

    /**
     * The property value that will be mapped to a Schema
     *
     * @return the property value
     **/
    String value() default "";

    /**
     * The schema that is being mapped to a property value
     *
     * @return the Schema reference
     **/
    Class<?> schema() default Void.class;

}
