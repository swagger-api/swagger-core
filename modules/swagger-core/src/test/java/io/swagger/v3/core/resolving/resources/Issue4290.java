package io.swagger.v3.core.resolving.resources;

import io.swagger.v3.oas.annotations.media.Schema;

public class Issue4290 {
    @Schema(description = "A string, a number or a boolean", anyOf = { String.class, Number.class, Boolean.class })
    public Object value;

}
