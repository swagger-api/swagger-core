package com.wordnik.swagger.model.override;

import com.wordnik.swagger.converter.ModelConverter;
import com.wordnik.swagger.converter.ModelConverterContext;
import com.wordnik.swagger.jackson.AbstractModelConverter;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.ModelImpl;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.util.Json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.*;

import scala.util.parsing.json.JSON;

public class GericModelConverter extends AbstractModelConverter {

	protected GericModelConverter() {
		super(Json.mapper());
	}

	@Override
	public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
		return chain.next().resolveProperty(type, context, annotations, chain);
	}

	@Override
	public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> next) {
		if(type instanceof Class<?>){
			Class<?> cls = (Class<?>) type;
			if(GenericModel.class.isAssignableFrom(cls)){
				ModelImpl impl = new ModelImpl();
				impl.setName(cls.getSimpleName());				
				for (Entry<String, Class<?>> entry : GenericModel.getDeclaredProperties().entrySet()) {
					impl.addProperty(entry.getKey(),context.resolveProperty(entry.getValue(), null));
				}
				context.defineModel(impl.getName(), impl);
				return impl;
			}
		}
		return null;
	}
}