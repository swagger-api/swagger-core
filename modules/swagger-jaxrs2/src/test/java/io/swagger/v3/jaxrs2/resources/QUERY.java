package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stands in for the {@code @QUERY} binding an application declares itself, since JAX-RS /
 * Jakarta REST does not ship one for the OpenAPI 3.2 {@code QUERY} method.
 *
 * <p>It is meta-annotated with {@link HttpMethod} in the same way {@code javax.ws.rs.PATCH} is,
 * which is all the reader needs: {@code ReaderUtils.getHttpMethodFromCustomAnnotations} resolves
 * any such annotation to its lower-cased method name, which is then mapped onto
 * {@link io.swagger.v3.oas.models.PathItem#getQuery()}.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("QUERY")
public @interface QUERY {
}
