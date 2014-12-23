package com.wordnik.swagger.converter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

public class ModelConverterContextImpl implements ModelConverterContext {
	private final List<ModelConverter> converters;
	private final Map<String, Model> modelByName;
	
	public ModelConverterContextImpl(List<ModelConverter> converters ) {
		this.converters = converters;
		modelByName = new TreeMap<String, Model>();
	}
	
	
	@Override
	public void defineModel(String name, Model model) {
		modelByName.put(name, model);
	}
	
	public Map<String,Model> getDefinedModels(){
		return Collections.unmodifiableMap(modelByName);
	}

	@Override
	public Property resolveProperty(Type type) {
		for (ModelConverter modelConverter : converters) {
			Property resolvedProperty = modelConverter.resolveProperty(type,this);
			if(resolvedProperty!=null){
				return resolvedProperty;
			}
		}
		return null;
	}

	@Override
	public Model resolve(Type type) {
		for (ModelConverter modelConverter : converters) {
			Model resolve = modelConverter.resolve(type,this);
			if(resolve!=null){
				return resolve;
			}
		}
		return null;
	}

}
