package io.swagger.v3.oas.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * The annotation may be used to define a method as an OpenAPI Webhook.
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.1.0/versions/3.1.0.md#oasWebhooks">Webhook (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.OpenAPIDefinition
 **/
@Target({METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@OpenAPI31
public @interface Webhook {
    /**
     * Provides the name related to this webhook.
     * @return webhook name
     */
    String name();

    /**
     * Provides the operation related to this webhook.
     * @return webhook operation
     */
    Operation operation();

}
