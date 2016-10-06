package io.swagger.annotations;

/**
 * Annotation used to construct OAuth security definition.
 */
public @interface OAuth2Definition {

    /**
     * Key used to refer to this security definition
     *
     * @return key used to refer to this security definition
     */
    String key();

    /**
     * A short description for security scheme.
     *
     * @return a short description for security scheme.
     */
    String description() default "";

    /**
     * The flow used by the OAuth2 security scheme.
     * Valid values are "implicit", "password", "application" or "accessCode".
     *
     * @return
     */
    Flow flow();

    /**
     * The authorization URL to be used for this flow. This SHOULD be in the form of a URL.
     * Required for implicit and access code flows
     *
     * @return the authorization URL to be used for this flow. This SHOULD be in the form of a URL.
     */
    String authorizationUrl() default "";

    /**
     * The token URL to be used for this flow. This SHOULD be in the form of a URL.
     * Required for password, applcation, and access code flows.
     *
     * @return the token URL to be used for this flow. This SHOULD be in the form of a URL.
     */
    String tokenUrl() default "";

    Scope[] scopes() default {};

    enum Flow {
        IMPLICIT, ACCESS_CODE, PASSWORD, APPLICATION
    }
}
