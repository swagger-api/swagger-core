package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class ModelWithRanges {
    @Schema(description = "values with include range", minimum = "1", maximum = "5")
    public int getInclusiveRange() {
        return 2;
    }

    @Schema(description = "values with include range", minimum = "1", exclusiveMinimum = true, maximum = "5", exclusiveMaximum = true)
    public int getExclusiveRange() {
        return 2;
    }

    @Schema(description = "values with include range", minimum = "1")
    public int getPositiveInfinityRange() {
        return 2;
    }

    @Schema(description = "values with include range", maximum = "5")
    public int getNegativeInfinityRange() {
        return 2;
    }

    @Schema(description = "some string values", allowableValues = {"str1", "str2"})
    public String getStringValues() {
        return "test";
    }

    @Schema(description = "some string values", minimum = "1.0", maximum = "8.0", exclusiveMaximum = true)
    public Double getDoubleValues() {
        return 1.0;
    }
}
