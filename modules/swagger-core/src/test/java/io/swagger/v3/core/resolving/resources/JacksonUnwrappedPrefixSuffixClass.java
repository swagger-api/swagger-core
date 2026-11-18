package io.swagger.v3.core.resolving.resources;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;

public class JacksonUnwrappedPrefixSuffixClass {

    @JsonUnwrapped(prefix = "in_", suffix = "_x")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private JacksonUnwrappedRecord.Inner inner;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer number;

    public JacksonUnwrappedRecord.Inner getInner() {
        return inner;
    }

    public void setInner(JacksonUnwrappedRecord.Inner inner) {
        this.inner = inner;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
