package io.swagger.v3.core.resolving.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestObject3697 {
    @JsonProperty("id")
    private final Long id;

    @io.swagger.v3.oas.annotations.media.Schema(hidden = true)
    @JsonProperty("hidden")
    private final String hidden;


    @JsonCreator
    public TestObject3697(@JsonProperty("id") Long id,
                          @io.swagger.v3.oas.annotations.media.Schema(hidden = true)
                                              @JsonProperty("hidden") String hidden) {
        this.id = id;
        this.hidden = hidden;
    }

    public Long getId() {
        return id;
    }

    public String getHidden() {
        return hidden;
    }
}
