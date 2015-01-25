package com.wordnik.swagger.converter;

import java.lang.reflect.Type;
import java.util.Iterator;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

public interface ModelConverter {
	
	/**
	 * 
	 * @param type
	 * @param context
   * @param chain the chain of model converters to try if this implementation cannot process
	 * @return null if this ModelConverter cannot convert the given Type
	 */
	public Property resolveProperty(Type type, ModelConverterContext context, Iterator<ModelConverter> chain);
	
	/**
	 * 
	 * @param type
	 * @param context
   * @param chain the chain of model converters to try if this implementation cannot process
	 * @return null if this ModelConverter cannot convert the given Type
	 */
	public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain);
}