package io.swagger.v3.jaxrs2.resources.siblings;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "parent")
public class Category {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
