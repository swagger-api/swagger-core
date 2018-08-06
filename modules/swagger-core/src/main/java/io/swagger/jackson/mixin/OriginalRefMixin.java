package io.swagger.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class OriginalRefMixin {

    @JsonIgnore
    public abstract String get$ref();

    @JsonGetter("$ref")
    public abstract String getOriginalRef();

}
