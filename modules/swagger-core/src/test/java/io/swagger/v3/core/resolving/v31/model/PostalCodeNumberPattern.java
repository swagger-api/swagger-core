package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.Schema;


public class PostalCodeNumberPattern {

    private Object postalCode;

    @Schema(
            pattern = "[0-9]{5}(-[0-9]{4})?",
            type = "string"
    )
    public Object getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Object postalCode) {
        this.postalCode = postalCode;
    }
}
