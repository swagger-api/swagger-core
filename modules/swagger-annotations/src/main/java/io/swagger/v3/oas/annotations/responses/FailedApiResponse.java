package io.swagger.v3.oas.annotations.responses;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.Content;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A meta-annotation that bundles common error response definitions for API operations.
 * <p>
 * Includes default definitions for:
 * <ul>
 *   <li>400 Bad Request</li>
 *   <li>401 Unauthorized</li>
 *   <li>403 Forbidden</li>
 *   <li>404 Not Found</li>
 *   <li>429 Too Many Requests</li>
 *   <li>500 Internal Server Error</li>
 *   <li>503 Service Unavailable</li>
 * </ul>
 * Can be used at type level to apply to all operations in a controller,
 * or at method level for individual operations.
 *
 * @see ApiResponse
 * @see ApiResponses
 * @since 2.2.32
 */

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
@ApiResponse(responseCode = "429", description = "Too Many Requests", content = @Content)
@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
@ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content)
public @interface FailedApiResponse {
    /**
     * A reference to a response definition in components responses.
     *
     * @return the reference
     * @since 2.2.32
     */
    String ref() default "";

    /**
     * The list of optional extensions.
     *
     * @return an optional array of extensions
     * @since 2.2.32
     */
    Extension[] extensions() default {};
}
