package io.swagger.v3.oas.annotations.info;

import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation may be used in {@link Info#contact()} to define a contact for the OpenAPI spec.
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#contactObject">Contact (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.OpenAPIDefinition
 * @see Info
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Contact {
    /**
     * The identifying name of the contact person/organization.
     *
     * @return the name of the contact
     **/
    String name() default "";

    /**
     * The URL pointing to the contact information. Must be in the format of a URL.
     *
     * @return the URL of the contact
     **/
    String url() default "";

    /**
     * The email address of the contact person/organization. Must be in the format of an email address.
     *
     * @return the email address of the contact
     **/
    String email() default "";

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

}
