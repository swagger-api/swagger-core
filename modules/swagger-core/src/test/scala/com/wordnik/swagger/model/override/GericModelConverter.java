package com.wordnik.swagger.model.override;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import scala.util.parsing.json.JSON;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.converter.ModelConverter;
import com.wordnik.swagger.converter.ModelConverterContext;
import com.wordnik.swagger.jackson.AbstractModelConverter;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.ModelImpl;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.util.Json;

public class GericModelConverter extends AbstractModelConverter {

	protected GericModelConverter() {
		super(Json.mapper());
	}

	@Override
	public Property resolveProperty(Type type, ModelConverterContext context) {
		return null;
	}

	@Override
	public Model resolve(Type type, ModelConverterContext context) {
		if(type instanceof Class<?>){
			Class<?> cls = (Class<?>) type;
			if(GenericModel.class.isAssignableFrom(cls)){
				ModelImpl impl = new ModelImpl();
				impl.setName(cls.getSimpleName());				
				for (Entry<String, Class<?>> entry : GenericModel.getDeclaredProperties().entrySet()) {
					impl.addProperty(entry.getKey(),context.resolveProperty(entry.getValue()));
				}
				context.defineModel(impl.getName(), impl);
				return impl;
			}
		}
		return null;
	}
}