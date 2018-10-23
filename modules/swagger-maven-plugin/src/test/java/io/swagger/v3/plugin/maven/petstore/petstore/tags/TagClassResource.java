package io.swagger.v3.plugin.maven.petstore.petstore.tags;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Class with Tag Annotations at Class level
 */
@Tag(name = "Second Tag")
@Tag(name = "Fourth Tag Full", description = "desc class", externalDocs = @ExternalDocumentation(description = "docs desc class"))
@Tag(name = "Fifth Tag Full", description = "desc class", externalDocs = @ExternalDocumentation(description = "docs desc class"))
@Tag(name = "Sixth Tag")
public class TagClassResource {
}
