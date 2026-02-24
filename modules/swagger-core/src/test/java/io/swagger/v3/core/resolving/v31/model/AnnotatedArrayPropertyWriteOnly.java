package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class AnnotatedArrayPropertyWriteOnly {

    private List<String> randomList;

    @ArraySchema(
            schema = @Schema(
                    types = { "string" },
                    description = "itemdescription",
                    title = "itemtitle"

            ),
            arraySchema = @Schema(
                    description = "arraydescription",
                    title = "arraytitle",
                    accessMode = Schema.AccessMode.WRITE_ONLY,
                    examples = "Jane"),
            maxContains = 5,
            minContains = 1
    )
    public List<String> getRandomList() {
        return randomList;
    }
}
