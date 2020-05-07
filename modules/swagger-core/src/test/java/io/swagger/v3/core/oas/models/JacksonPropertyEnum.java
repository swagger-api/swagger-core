package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum JacksonPropertyEnum {
    @JsonProperty("p1") PRIVATE,
    @JsonProperty("p2") PUBLIC,
    SYSTEM,
    INVITE_ONLY
}
