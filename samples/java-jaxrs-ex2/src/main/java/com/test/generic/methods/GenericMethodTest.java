package com.test.generic.methods;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class GenericMethodTest {
	public static void main(String[] args) {
		doMagic(ActualService.class);
		doMagic(ActualService2.class);
	}
	
	private static void doMagic(Class<?> clazz) {
		// Mapping the available generic types to their actual types
		Map<Type, Type> typesMap = new HashMap<Type, Type>();
		TypeVariable<?>[] typeParameters = clazz.getSuperclass().getTypeParameters();
		Type[] actualTypeArguments = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
		
		for (int i = 0; i < typeParameters.length; i++) {
			typesMap.put(typeParameters[i], actualTypeArguments[i]);
		}
		
		// Mapping typed methods to their actual class values
		for (Method method: clazz.getMethods()) {
			Type[] genericParameterTypes = method.getGenericParameterTypes();
			
			for (Type type : genericParameterTypes) {
				Type foundType = typesMap.get(type);
				
				if (foundType != null) {
					System.out.println("OMG! I matched a method generic type! It is: " + foundType);
				}
			}
		}
	}
}
