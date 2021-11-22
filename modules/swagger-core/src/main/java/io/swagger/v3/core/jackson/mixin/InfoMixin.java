package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class InfoMixin {

    @JsonIgnore
    public abstract String getSummary();
}
