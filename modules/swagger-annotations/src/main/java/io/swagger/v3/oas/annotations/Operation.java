package io.swagger.v3.oas.annotations;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * The annotation may be used to define a resource method as an OpenAPI Operation, and/or to define additional
 * properties for the Operation.
 *
 * <p>Note: swagger-jaxrs2 reader engine includes by default also methods of scanned resources which are not annotated
 * with @Operation, as long as a jax-rs @Path is defined at class and/or method level, together with the http method
 * annotation (@GET, @POST, etc).</p>
 * <p>This behaviour is controlled by configuration property `scanAllResources` which defaults to true. By setting this
 * flag to false only @{@link Operation} annotated methods are considered.</p>
 *
 * <p>The following fields can also alternatively be defined at method level (as repeatable annotations in case of arrays),
 * in this case method level annotations take precedence over @{@link Operation} annotation fields:</p>
 *
 * <ul>
 * <li>tags: @{@link io.swagger.v3.oas.annotations.tags.Tag}</li>
 * <li>externalDocs: @{@link ExternalDocumentation}</li>
 * <li>parameters: @{@link Parameter}</li>
 * <li>responses: @{@link ApiResponse}</li>
 * <li>security: @{@link SecurityRequirement}</li>
 * <li>servers: @{@link Server}</li>
 * <li>extensions: @{@link Extension}</li>
 * <li>hidden: @{@link Hidden}</li>
 * </ul>
 *
 * @see <a target="_new" href="https://github.com/OAI/OpenAPI-Specification/blob/3.0.4/versions/3.0.4.md#operation-object">Operation (OpenAPI specification)</a>
 **/
@Target({METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Operation {
    /**
     * The HTTP method for this operation.
     *
     * @return the HTTP method of this operation
     **/
    String method() default "";

    /**
     * Tags can be used for logical grouping of operations by resources or any other qualifier.
     *
     * @return the list of tags associated with this operation
     **/
    String[] tags() default {};

    /**
     * Provides a brief description of this operation. Should be 120 characters or less for proper visibility in Swagger-UI.
     *
     * @return a summary of this operation
     **/
    String summary() default "";

    /**
     * A verbose description of the operation.
     *
     * @return a description of this operation
     **/
    String description() default "";

    /**
     * Request body associated to the operation.
     *
     * @return a request body.
     */
    RequestBody requestBody() default @RequestBody();

    /**
     * Additional external documentation for this operation.
     *
     * @return additional documentation about this operation
     **/
    ExternalDocumentation externalDocs() default @ExternalDocumentation();

    /**
     * The operationId is used by third-party tools to uniquely identify this operation.
     *
     * @return the ID of this operation
     **/
    String operationId() default "";

    /**
     * An optional array of parameters which will be added to any automatically detected parameters in the method itself.
     *
     * @return the list of parameters for this operation
     **/
    Parameter[] parameters() default {};

    /**
     * The list of possible responses as they are returned from executing this operation.
     *
     * @return the list of responses for this operation
     **/
    ApiResponse[] responses() default {};

    /**
     * Allows an operation to be marked as deprecated.  Alternatively use the @Deprecated annotation
     *
     * @return whether or not this operation is deprecated
     **/
    boolean deprecated() default false;

    /**
     * A declaration of which security mechanisms can be used for this operation.
     *
     * @return the array of security requirements for this Operation
     */
    SecurityRequirement[] security() default {};

    /**
     * An alternative server array to service this operation.
     *
     * @return the list of servers hosting this operation
     **/
    Server[] servers() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

    /**
     * Allows this operation to be marked as hidden
     *
     * @return whether or not this operation is hidden
     */
    boolean hidden() default false;

    /**
     * Ignores JsonView annotations while resolving operations and types.
     *
     * @return whether or not to ignore JsonView annotations
     */
    boolean ignoreJsonView() default false;
}
