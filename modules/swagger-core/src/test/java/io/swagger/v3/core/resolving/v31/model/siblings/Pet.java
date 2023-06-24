package io.swagger.v3.core.resolving.v31.model.siblings;

import io.swagger.v3.oas.annotations.media.Schema;

public class Pet {
    private Category category;

    @Schema(description = "child")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
