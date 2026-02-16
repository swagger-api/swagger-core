package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

/**
 * Demonstrates workarounds for issue #5014 until JSON parsing is implemented.
 * These are valid approaches you can use TODAY to work around the limitation.
 */
public class ModelWithExampleWorkarounds {

    /**
     * WORKAROUND 1: Use a single 'example' (not 'examples') with nested JSON
     * The example will be a string, but Swagger UI can parse it for display.
     */
    @Schema(
            description = "Workaround 1: Single example with array-of-arrays",
            example = "[[\"one\",\"two\",\"three\"], [\"four\",\"five\",\"six\"]]"
    )
    private List<List<String>> singleExampleWorkaround;

    /**
     * WORKAROUND 1b: Single example for simple array
     */
    @Schema(
            description = "Workaround 1b: Single example as JSON array",
            example = "[\"red\", \"green\", \"blue\"]"
    )
    private List<String> singleArrayExample;

    /**
     * For demonstration: This is what we want to work but doesn't yet.
     * Multiple examples with arrays - currently emits as strings.
     */
    @Schema(
            description = "What we want (doesn't work yet): Multiple array examples",
            examples = {"[\"one\",\"two\",\"three\"]", "[\"four\",\"five\",\"six\"]"}
    )
    private List<String> multipleExamplesNotWorking;

    public List<List<String>> getSingleExampleWorkaround() {
        return singleExampleWorkaround;
    }

    public void setSingleExampleWorkaround(List<List<String>> singleExampleWorkaround) {
        this.singleExampleWorkaround = singleExampleWorkaround;
    }

    public List<String> getSingleArrayExample() {
        return singleArrayExample;
    }

    public void setSingleArrayExample(List<String> singleArrayExample) {
        this.singleArrayExample = singleArrayExample;
    }

    public List<String> getMultipleExamplesNotWorking() {
        return multipleExamplesNotWorking;
    }

    public void setMultipleExamplesNotWorking(List<String> multipleExamplesNotWorking) {
        this.multipleExamplesNotWorking = multipleExamplesNotWorking;
    }
}
