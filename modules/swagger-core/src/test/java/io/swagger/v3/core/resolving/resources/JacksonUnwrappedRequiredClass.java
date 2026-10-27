package io.swagger.v3.core.resolving.resources;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;

public class JacksonUnwrappedRequiredClass {

    @JsonUnwrapped
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private JacksonUnwrappedRecord.Inner inner;

    public JacksonUnwrappedRecord.Inner getInner() {
        return inner;
    }

    public void setInner(JacksonUnwrappedRecord.Inner inner) {
        this.inner = inner;
    }
}
