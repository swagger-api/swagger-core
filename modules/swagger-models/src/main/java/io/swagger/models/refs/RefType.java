package io.swagger.models.refs;

/**
 * Created by russellb337 on 7/1/15.
 */
public enum RefType {
    DEFINITION("#/definitions/"),
    PARAMETER("#/parameters/"),
    PATH("#/paths/"),
    RESPONSE("#/responses/");

    private final String internalPrefix;

    private RefType(final String prefix) {
        this.internalPrefix = prefix;
    }

    /**
     * The prefix in an internal reference of this type.
     */
    public String getInternalPrefix() {
        return internalPrefix;
    }
}
