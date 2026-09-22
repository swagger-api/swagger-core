package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom HTTP method binding that has no fixed Path Item field. Unlike the fixed
 * methods, the declared case must survive: {@code PURGE} is stored in
 * {@link io.swagger.v3.oas.models.PathItem#getAdditionalOperations()} keyed by its
 * original method name (OpenAPI 3.2).
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("PURGE")
public @interface PURGE {
}
