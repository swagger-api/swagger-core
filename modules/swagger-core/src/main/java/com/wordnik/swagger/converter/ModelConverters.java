package com.wordnik.swagger.converter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.jackson.ModelResolver;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.util.Json;

public class ModelConverters {
	private static final ModelConverters SINGLETON = new ModelConverters();
	private final List<ModelConverter> converters;

	public static ModelConverters getInstance() {
		return SINGLETON;
	}
	
	public ModelConverters() {
		converters = new CopyOnWriteArrayList<ModelConverter>();
		converters.add(new ModelResolver(Json.mapper()));
	}
	
	public void addConverter(ModelConverter converter){
		converters.add(0,converter);
	}
	
	public void removeConverter(ModelConverter converter){
		converters.remove(converter);
	}

	public Property readAsProperty(Type type) {
		ModelConverterContextImpl context = new ModelConverterContextImpl(
				converters);
		return context.resolveProperty(type);
	}

	public Map<String, Model> read(Type type) {
		Map<String, Model> modelMap = new HashMap<String, Model>();
		if (shouldProcess(type)) {
			ModelConverterContextImpl context = new ModelConverterContextImpl(
					converters);
			Model resolve = context.resolve(type);
			for (Entry<String, Model> entry : context.getDefinedModels()
					.entrySet()) {
				if (entry.getValue().equals(resolve)) {
					modelMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return modelMap;
	}

	public Map<String, Model> readAll(Type type) {
		if (shouldProcess(type)) {
			ModelConverterContextImpl context = new ModelConverterContextImpl(
					converters);
			context.resolve(type);
			return context.getDefinedModels();
		}
		return new HashMap<String, Model>();
	}

	private boolean shouldProcess(Type type) {
		if(type instanceof Class<?>){
			Class<?> cls = (Class<?>) type;
			return !cls.getName().startsWith("java.lang");		
		}
		return false;
	}
}