package io.swagger.v3.plugin.maven.petstore.petstore.tags;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Tag in the OpenAPIDefinition annotation
 */
@OpenAPIDefinition(tags = {
        @Tag(name = "Definition First Tag"),
        @Tag(name = "Definition Second Tag full", description = "desc definition")
})
public class TagOpenAPIDefinitionResource {
}
