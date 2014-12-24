package com.wordnik.swagger.converter;

import java.lang.reflect.Type;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

public interface ModelConverterContext {
	
	/**
	 * needs to be called whenever a Model is defined which can be referenced from another 
	 * Model or Property
	 * 
	 * @param name the name of the model
	 * @param model the Model
	 */
	public void defineModel(String name, Model model);
	
	/**
	 * @param cls the property Class
	 * @return a property representation of the Class. Any referenced models will be defined already.
	 */
	public 	Property resolveProperty(Type type);

	/** 
	 * @param cls The Model Class
	 * @return a Model representation of the Class. Any referenced models will be defined already.
	 */
	public  Model resolve(Type type);
}