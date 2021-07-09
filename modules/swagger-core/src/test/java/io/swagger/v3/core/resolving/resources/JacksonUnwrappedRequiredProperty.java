package io.swagger.v3.core.resolving.resources;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class JacksonUnwrappedRequiredProperty {
    @JsonUnwrapped private final InnerTypeRequired innerType;

    public JacksonUnwrappedRequiredProperty(InnerTypeRequired innerType) {
      this.innerType = innerType;
    }

    public InnerTypeRequired getInnerType() {
      return innerType;
    }
}
