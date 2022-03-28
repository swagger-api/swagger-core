package io.swagger.v3.oas.annotations.servers;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Container for repeatable {@link Server} annotation
 *
 * @see Server
 */
@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Servers {
    /**
     * An array of Server Objects which is used to provide connectivity information to a target server.
     *
     * @return the servers used for this API
     */
    Server[] value() default {};
}
