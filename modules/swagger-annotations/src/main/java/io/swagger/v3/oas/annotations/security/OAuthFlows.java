package io.swagger.v3.oas.annotations.security;

import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows configuration of the supported OAuth Flows.
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OAuthFlows {
    /**
     * Configuration for the OAuth Implicit flow.
     *
     * @return OAuthFlow implicit
     **/
    OAuthFlow implicit() default @OAuthFlow();

    /**
     * Configuration for the OAuth Resource Owner Password flow.
     *
     * @return OAuthFlow password
     **/
    OAuthFlow password() default @OAuthFlow();

    /**
     * Configuration for the OAuth Client Credentials flow.
     *
     * @return OAuthFlow clientCredentials
     **/
    OAuthFlow clientCredentials() default @OAuthFlow();

    /**
     * Configuration for the OAuth Authorization Code flow.
     *
     * @return OAuthFloe authorizationCode
     **/
    OAuthFlow authorizationCode() default @OAuthFlow();

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

}
