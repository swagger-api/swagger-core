package io.swagger.v3.oas.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Container for repeatable {@link Webhook} annotation
 *
 * @see Webhook
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@OpenAPI31
public @interface Webhooks {

    /**
     * An array of Webhook annotations
     *
     * @return the array of the Webhook
     **/
    Webhook[] value() default {};
}
