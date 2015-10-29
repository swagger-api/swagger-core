package io.swagger.models.parameters;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.models.properties.PropertyBuilder.PropertyId;

public class SerializableParameterTest {
	
	@Test
	public void testParameters() throws NoSuchMethodException, SecurityException {
		
		String[] requredMethods = {
				"maximum",
			    "exclusiveMaximum",
			    "minimum",
			    "exclusiveMinimum",
			    "maxLength",
			    "minLength",
			    "pattern",
			    "maxItems",
			    "minItems",
			    "uniqueItems",
			    "multipleOf"};
		
	    
		Method[] methods = SerializableParameter.class.getMethods();
		
		String[] methodPrefix = { "get", "set" };
		
		for (String methodName : requredMethods) {
			methodName = capitalize(methodName);
			for (String mp : methodPrefix) {
				String methodNameWithPrefix = mp + methodName;
				boolean found = false;
				for (Method method : methods) {
					if (method.getName().equals(methodNameWithPrefix)) {
						found = true;
						break;
					}
				}
				Assert.assertTrue(found,methodNameWithPrefix + " declaration is missing from SerializableParameter interface");
			}
		}
	}

	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}
}
