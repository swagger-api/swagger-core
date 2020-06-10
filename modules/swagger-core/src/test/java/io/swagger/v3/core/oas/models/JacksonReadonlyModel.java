package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JacksonReadonlyModel {
    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    public Integer getCount() {
        return null;
    }

    @JsonIgnore
    public void setCount(Integer count) {

    }
}