package com.wordnik.swagger.converter;

import java.lang.reflect.Type;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

public interface ModelConverter {
	
	/**
	 * 
	 * @param type
	 * @param context
	 * @return null if this ModelConverter cannot convert the given Type
	 */
	public Property resolveProperty(Type type, ModelConverterContext context);
	
	/**
	 * 
	 * @param type
	 * @param context
	 * @return null if this ModelConverter cannot convert the given Type
	 */
	public Model resolve(Type type, ModelConverterContext context);
}