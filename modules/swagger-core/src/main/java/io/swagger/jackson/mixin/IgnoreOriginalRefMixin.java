package io.swagger.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class IgnoreOriginalRefMixin {

    @JsonIgnore
    public abstract String getOriginalRef();

}
