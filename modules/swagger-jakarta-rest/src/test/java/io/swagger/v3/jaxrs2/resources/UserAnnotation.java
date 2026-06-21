package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Operation(tags = {"test"})
public @interface UserAnnotation {
    /**
     * The identifying name of the contact person/organization.
     *
     * @return the name of the contact
     **/
    String name() default "";

}
