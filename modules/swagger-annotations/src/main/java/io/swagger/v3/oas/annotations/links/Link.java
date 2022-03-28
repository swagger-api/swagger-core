package io.swagger.v3.oas.annotations.links;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.servers.Server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation may be applied in {@link io.swagger.v3.oas.annotations.responses.ApiResponse#links()} to add OpenAPI links to a response.
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#linkObject">Link (OpenAPI specification)</a>
 * @see io.swagger.v3.oas.annotations.responses.ApiResponse
 **/
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Link {
    /**
     * The name of this link.
     *
     * @return the link's name
     **/
    String name() default "";

    /**
     * A relative or absolute reference to an OAS operation. This field is mutually exclusive of the operationId field, and must point to an Operation Object. Relative operationRef values may be used to locate an existing Operation Object in the OpenAPI definition.  Ignored if the operationId property is specified.
     *
     * @return an operation reference
     **/
    String operationRef() default "";

    /**
     * The name of an existing, resolvable OAS operation, as defined with a unique operationId. This field is mutually exclusive of the operationRef field.
     *
     * @return an operation ID
     **/
    String operationId() default "";

    /**
     * Array of parameters to pass to an operation as specified with operationId or identified via operationRef.
     *
     * @return the list of parameters for this link
     **/
    LinkParameter[] parameters() default {};

    /**
     * A description of the link. CommonMark syntax may be used for rich text representation.
     *
     * @return the link's description
     **/
    String description() default "";

    /**
     * A literal value or {expression} to use as a request body when calling the target operation.
     *
     * @return the request body of this link
     **/
    String requestBody() default "";

    /**
     * An alternative server to service this operation.
     *
     * @return the server associated to this link
     **/
    Server server() default @Server;

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * A reference to a link defined in components links.
     *
     * @since 2.0.3
     * @return the reference
     **/
    String ref() default "";

}
