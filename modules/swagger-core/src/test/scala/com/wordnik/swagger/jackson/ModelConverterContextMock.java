package com.wordnik.swagger.jackson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.wordnik.swagger.converter.ModelConverter;
import com.wordnik.swagger.converter.ModelConverterContext;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

public class ModelConverterContextMock implements ModelConverterContext {
	public Map<String,Model> models = new HashMap<String,Model>();
	public ModelConverter delegate;
	
	@Override
	public void defineModel(String name, Model model) {
		models.put(name, model);
	}

	@Override
	public Property resolveProperty(Type type) {
		// TODO Auto-generated method stub
		return delegate.resolveProperty(type, this);
	}

	@Override
	public Model resolve(Type type) {
		// TODO Auto-generated method stub
		return delegate.resolve(type, this);
	}

}
