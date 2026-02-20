package io.swagger.v3.core.resolving.resources;

import io.swagger.v3.oas.annotations.media.Schema;

public class InnerTypeRequired {
    public int foo;
    @Schema(required = true)
    public String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
}
