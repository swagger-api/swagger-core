package io.swagger.v3.oas.annotations.security;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * The annotation may be applied at class or method level, or in {@link io.swagger.v3.oas.annotations.Operation#security()} to define security requirements for the
 * single operation (when applied at method level) or for all operations of a class (when applied at class level).
 * <p>It can also be used in {@link io.swagger.v3.oas.annotations.OpenAPIDefinition#security()} to define spec level security.</p>
 * <p>{@link SecurityRequirement#combine()} can be used to define multiple security requirements at the same time, requiring each one of them.
 * If only one of multiple security schemes is required, use multiple {@link SecurityRequirement} annotations.</p>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#security-requirement-object">Security Requirement (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.OpenAPIDefinition
 * @see io.swagger.v3.oas.annotations.Operation
 **/
@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(SecurityRequirements.class)
@Inherited
public @interface SecurityRequirement {
    /**
     * This name must correspond to a declared SecurityRequirement.
     * <p>Exactly one of this and {@link #combine()} must be set.</p>
     *
     * @return String name
     */
    String name() default "";

    /**
     * If the security scheme is of type "oauth2" or "openIdConnect", then the value is a list of scope names required for the execution.
     * For other security scheme types, the array must be empty.
     *
     * @return String array of scopes
     */
    String[] scopes() default {};

    /**
     * If multiple requirements apply at the same time, use this value instead of {@link #name()} and {@link #scopes()}.
     * If any one of multiple security schemes is required, use multiple {@link SecurityRequirement} annotations instead.
     * <p>Exactly one of this and {@link #name()} must be set.</p>
     *
     * @return SecurityRequirementEntry array of requirements
     */
    SecurityRequirementEntry[] combine() default {};
}
