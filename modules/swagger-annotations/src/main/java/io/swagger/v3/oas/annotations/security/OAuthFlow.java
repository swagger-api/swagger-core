package io.swagger.v3.oas.annotations.security;

import io.swagger.v3.oas.annotations.extensions.Extension;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration details for a supported OAuth Flow.
 **/
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OAuthFlow {
    /**
     * The authorization URL to be used for this flow. This must be in the form of a URL.  Applies to oauth2 ("implicit", "authorizationCode") type.
     *
     * @return the authorization url
     **/
    String authorizationUrl() default "";

    /**
     * The token URL to be used for this flow. This must be in the form of a URL.  Applies to oauth2 ("password", "clientCredentials", "authorizationCode") type.
     *
     * @return the token url
     **/
    String tokenUrl() default "";

    /**
     * The URL to be used for obtaining refresh tokens. This must be in the form of a URL.  Applies to oauth2 type.
     *
     * @return the refresh url
     **/
    String refreshUrl() default "";

    /**
     * The available scopes for the OAuth2 security scheme.  Applies to oauth2 type.
     *
     * @return array of scopes
     **/
    OAuthScope[] scopes() default {};

    /**
     * The list of optional extensions
     *
     * @return an optional array of extensions
     */
    Extension[] extensions() default {};

}
