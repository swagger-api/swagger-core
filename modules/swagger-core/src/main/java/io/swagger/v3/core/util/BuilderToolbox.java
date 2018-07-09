package io.swagger.v3.core.util;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.reflect.Type;
import java.util.Map;

public class BuilderToolbox {
	public static void addModel(OpenAPI api, Type type) {
		if (api.getComponents() == null) {
			api.components(new Components());
		}

		ResolvedSchema resolvedSchema = ModelConverters.getInstance()
				.resolveAsResolvedSchema(
						new AnnotatedType(type).resolveAsRef(true)
				);

		if (resolvedSchema.schema != null) {
			Map<String, Schema> schemaMap = resolvedSchema.referencedSchemas;
			if (schemaMap != null) {
				schemaMap.forEach((key, schema) -> api.getComponents().addSchemas(key, schema));
			}
		}
	}

	public Schema enumarizedSchema(Schema schema) {

		return schema;
	}
}
