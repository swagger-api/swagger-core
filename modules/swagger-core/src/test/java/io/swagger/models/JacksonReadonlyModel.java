package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JacksonReadonlyModel {
    @JsonProperty
    public Integer getCount() {
        return null;
    }

    @JsonIgnore
    public void setCount(Integer count) {

    }
}