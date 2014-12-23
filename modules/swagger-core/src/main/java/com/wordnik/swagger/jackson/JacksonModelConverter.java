package com.wordnik.swagger.jackson;

import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.converter.ModelConverter;
import com.wordnik.swagger.converter.ModelConverterContext;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.util.Json;

public class JacksonModelConverter implements ModelConverter {
	private final ObjectMapper mapper = Json.mapper();
	
	@Override
	public Property resolveProperty(Type type,ModelConverterContext context) {
		return new ModelResolver(mapper).resolveProperty(type,context);
	}

	@Override
	public Model resolve(Type type,ModelConverterContext context) {
		return new ModelResolver(mapper).resolve(type,context);
	}
}