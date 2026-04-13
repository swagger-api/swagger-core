package io.swagger.v3.oas.annotations.security;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation may be applied in {@link SecurityRequirement#combine()} to define combined security requirements for the
 * single operation.
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#security-requirement-object">Security Requirement (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.security.SecurityRequirement
 **/
@Target({ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityRequirementEntry {
    /**
     * This name must correspond to a declared SecurityRequirement.
     *
     * @return String name
     */
    String name();

    /**
     * If the security scheme is of type "oauth2" or "openIdConnect", then the value is a list of scope names required for the execution.
     * For other security scheme types, the array must be empty.
     *
     * @return String array of scopes
     */
    String[] scopes() default {};
}
