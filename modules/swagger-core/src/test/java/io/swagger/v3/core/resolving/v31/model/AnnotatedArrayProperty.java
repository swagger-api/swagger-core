package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class AnnotatedArrayProperty {

        private List<String> randomList;

        @ArraySchema(
                schema = @Schema(
                        types = { "string" },
                        description = "itemdescription",
                        title = "itemtitle"

                ),
                arraySchema = @Schema(description = "arraydescription", title = "arraytitle"),
                maxContains = 10,
                minContains = 1,
                contains = @Schema(
                        types = "string"
                ),
                unevaluatedItems = @Schema(
                        types = "number"
                ),
                prefixItems = {
                        @Schema(
                                description = "prefixdescription",
                                types = "string"
                        )
                }
        )
        public List<String> getRandomList() {
                return randomList;
        }
}
