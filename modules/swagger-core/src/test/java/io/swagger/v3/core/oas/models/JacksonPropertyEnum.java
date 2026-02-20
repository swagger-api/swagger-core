package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;

public enum JacksonPropertyEnum {
    @JsonProperty("p1") PRIVATE,
    @JsonProperty("p2") PUBLIC,
    SYSTEM,
    INVITE_ONLY,
    @Hidden HIDDEN
}
