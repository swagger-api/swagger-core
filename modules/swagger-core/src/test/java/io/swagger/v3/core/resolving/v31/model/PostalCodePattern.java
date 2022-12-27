package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class PostalCodePattern {

    private Object postalCode;

    @Schema(
            pattern = "[A-Z][0-9][A-Z] [0-9][A-Z][0-9]"
    )
    public Object getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Object postalCode) {
        this.postalCode = postalCode;
    }
}
