package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ModelWithInvalidJsonExamples {

    @Schema(
            description = "Field with invalid JSON examples (should remain as strings)",
            examples = {"[invalid json", "not a json array]"}
    )
    private List<String> invalidJsonExamples;

    @Schema(
            description = "Mixed valid and invalid JSON",
            examples = {"[\"valid\",\"json\"]", "invalid}"}
    )
    private List<String> mixedExamples;

    public List<String> getInvalidJsonExamples() {
        return invalidJsonExamples;
    }

    public void setInvalidJsonExamples(List<String> invalidJsonExamples) {
        this.invalidJsonExamples = invalidJsonExamples;
    }

    public List<String> getMixedExamples() {
        return mixedExamples;
    }

    public void setMixedExamples(List<String> mixedExamples) {
        this.mixedExamples = mixedExamples;
    }
}
