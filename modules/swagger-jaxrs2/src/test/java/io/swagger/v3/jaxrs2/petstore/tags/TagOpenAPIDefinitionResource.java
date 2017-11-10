package io.swagger.v3.jaxrs2.petstore.tags;

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
