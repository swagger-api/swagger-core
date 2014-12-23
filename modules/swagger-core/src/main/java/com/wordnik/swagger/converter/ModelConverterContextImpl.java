package com.wordnik.swagger.converter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

public class ModelConverterContextImpl implements ModelConverterContext {
	private static final Logger LOGGER = LoggerFactory.getLogger(ModelConverterContextImpl.class);
	
	private final List<ModelConverter> converters;
	private final Map<String, Model> modelByName;
	private final HashMap<Type,Model> modelByType;
	private final Set<Type> processedTypes;
	
	
	public ModelConverterContextImpl(List<ModelConverter> converters ) {
		this.converters = converters;
		modelByName = new TreeMap<String, Model>();
		modelByType = new HashMap<Type, Model>();
		processedTypes = new HashSet<Type>();
	}
	
	public ModelConverterContextImpl(ModelConverter converter) {
		this(new ArrayList<ModelConverter>());
		converters.add(converter);
	}
	
	
	@Override
	public void defineModel(String name, Model model) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(String.format("definemodel %s %s", name,model));
		}
		
		modelByName.put(name, model);
	}
	
	public Map<String,Model> getDefinedModels(){
		return Collections.unmodifiableMap(modelByName);
	}

	@Override
	public Property resolveProperty(Type type) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(String.format("resolveProperty %s", type));
		}
		Property resolvedProperty = null;
		for (ModelConverter modelConverter : converters) {
			resolvedProperty = modelConverter.resolveProperty(type,this);
			if(resolvedProperty!=null){
				return resolvedProperty;
			}
		}		
		return null;
	}

	@Override
	public Model resolve(Type type) {		
		if(processedTypes.contains(type)){
			return modelByType.get(type);
		}else{
			processedTypes.add(type);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(String.format("resolve %s", type));
		}
		Model resolve = null;
		for (ModelConverter modelConverter : converters) {
			resolve = modelConverter.resolve(type,this);
			if(resolve!=null){
				break;
			}
		}
		modelByType.put(type, resolve);
		return resolve;
	}
}