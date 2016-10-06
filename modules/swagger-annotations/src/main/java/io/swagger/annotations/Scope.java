package io.swagger.annotations;

/**
 * The available scopes for an OAuth2 security scheme
 */
public @interface Scope {

    /**
     * The name of the scope
     *
     * @return the name of the scope
     */
    String name();

    /**
     * A short description of the scope
     *
     * @return a short description of the scope
     */
    String description();
}
