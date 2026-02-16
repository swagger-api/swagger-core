package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Demonstrates @ArraySchema with arraySchema containing examples.
 * 
 * This tests whether examples in @ArraySchema.arraySchema work correctly
 * (related to issue #5012 which was partially fixed in PR #5049).
 * 
 * Also tests if those examples are parsed as JSON when they contain array strings
 * (related to issue #5014).
 */
public class ModelWithArraySchemaExamples {

    /**
     * Using @ArraySchema with single example in arraySchema.
     * This should work after PR #5049.
     */
    @ArraySchema(
            schema = @Schema(
                    type = "string",
                    description = "Individual color name"
            ),
            arraySchema = @Schema(
                    description = "Array of color names",
                    example = "[\"red\", \"green\", \"blue\"]"
            )
    )
    private List<String> colorsWithSingleExample;

    /**
     * Using @ArraySchema with multiple examples in arraySchema.
     * This combines issues #5012 and #5014:
     * - #5012: arraySchema examples should appear in output (fixed in PR #5049)
     * - #5014: examples should be parsed as JSON, not kept as strings (NOT YET FIXED)
     */
    @ArraySchema(
            schema = @Schema(
                    type = "string",
                    description = "Individual word"
            ),
            arraySchema = @Schema(
                    description = "Triple of words",
                    examples = {
                            "[\"one\", \"two\", \"three\"]",
                            "[\"four\", \"five\", \"six\"]",
                            "[\"red\", \"blue\", \"yellow\"]"
                    }
            )
    )
    private List<String> tripleWithMultipleExamples;

    /**
     * For comparison: Using @Schema directly on the field with examples.
     * This is the approach that has the issue documented in #5014.
     */
    @Schema(
            description = "Direct schema on field",
            examples = {
                    "[\"alpha\", \"beta\", \"gamma\"]",
                    "[\"delta\", \"epsilon\", \"zeta\"]"
            }
    )
    private List<String> directSchemaWithExamples;

    public List<String> getColorsWithSingleExample() {
        return colorsWithSingleExample;
    }

    public void setColorsWithSingleExample(List<String> colorsWithSingleExample) {
        this.colorsWithSingleExample = colorsWithSingleExample;
    }

    public List<String> getTripleWithMultipleExamples() {
        return tripleWithMultipleExamples;
    }

    public void setTripleWithMultipleExamples(List<String> tripleWithMultipleExamples) {
        this.tripleWithMultipleExamples = tripleWithMultipleExamples;
    }

    public List<String> getDirectSchemaWithExamples() {
        return directSchemaWithExamples;
    }

    public void setDirectSchemaWithExamples(List<String> directSchemaWithExamples) {
        this.directSchemaWithExamples = directSchemaWithExamples;
    }
}
