package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@ArraySchema(
        maxContains = 10,
        minContains = 1,
        contains = @Schema(
                types = { "string" }
        ),
        unevaluatedItems = @Schema(
                types = { "number" }
        ),
        schema = @Schema(
                types = { "string" },
                description = "itemdescription"
        ),
        arraySchema = @Schema(description = "arraydescription"),
        prefixItems = {
                @Schema(
                        types = { "string" }
                )
        }
)
public class AnnotatedArray {
}
