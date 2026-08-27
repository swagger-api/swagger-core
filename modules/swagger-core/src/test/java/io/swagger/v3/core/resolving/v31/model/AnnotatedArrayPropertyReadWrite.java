package io.swagger.v3.core.resolving.v31.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class AnnotatedArrayPropertyReadWrite {

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
                    accessMode = Schema.AccessMode.READ_WRITE,
                    examples = "Bob"),
            maxContains = 8,
            minContains = 2
    )
    public List<String> getRandomList() {
        return randomList;
    }
}
