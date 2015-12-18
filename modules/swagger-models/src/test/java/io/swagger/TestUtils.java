package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.exceptions.MethodNotFoundException;

import io.swagger.models.Model;

public class TestUtils {
	private final static Logger LOGGER = Logger.getLogger(TestUtils.class.getName());

	private TestUtils() {

	}
	
	private static <T> Map<Field, Object> getpropertiesAndValues(Class<T> clazz,Set<String>exclusions,boolean defaultValues,boolean includeInherited) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Map<Field,Object>propertiesAndDefaultValues=new HashMap<Field, Object>();
		Field[] fields = PowerMockito.fields(clazz);
		for(Field field:fields){
			if(exclusions!=null && exclusions.contains(field.getName())) continue;
			if(!includeInherited&&field.getDeclaringClass()!=clazz)continue;
			if(Modifier.isStatic(field.getModifiers())) continue;
			Class<?> type=field.getType();
			Object value=null;
			if(defaultValues){
				value=getTypeDefaultValue(type);
			}else{
				value=getTypeNonDefaultValue(type);
			}
			propertiesAndDefaultValues.put(field, value);
		}
		return propertiesAndDefaultValues;
	}
	
	
	private static boolean implementsMethod(Class<?> clazz,String methodName){
		try{
			Method[] methods = PowerMockito.methods(clazz, new String[]{methodName});
			for(Method method:methods){
				if(method.getDeclaringClass().equals(clazz))return true;
			}
		}catch(MethodNotFoundException ex){
			return false;
		}
		return false;
	}
	public static <T> void testEquals(Class<T> clazz,Set<String>exclusions,boolean useInheritedFields) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		if(!implementsMethod(clazz, "equals"))return;
		System.out.println();
		System.out.println("**************testing equals on class: "+clazz.getName()+"*****************");
		System.out.println("eclusions: "+exclusions);
		T instance=Whitebox.newInstance(clazz);//clazz.newInstance();
		T other=Whitebox.newInstance(clazz);
		Map<Field, Object> propertiesAndDefaultValues=getpropertiesAndValues(clazz,exclusions,true,useInheritedFields);
		Map<Field, Object> propertiesAndNonDefaultValues=getpropertiesAndValues(clazz,exclusions,false,useInheritedFields);
		testEquals(instance, other, propertiesAndDefaultValues,propertiesAndNonDefaultValues);
		System.out.println("**************successfully testedq equals on class: "+clazz.getName()+"*****************");
	}

	private static <T> Map<Field, Object> getpropertiesAndValues(Class<T> clazz, Set<String> exclusions, boolean defaultValues) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		return getpropertiesAndValues(clazz, exclusions, defaultValues, false);
	}

	public static <T extends Model> void testClone(T instance, Object[] propertiesAndDefaultValues) {
		assertTrue(instance.equals(instance));
		assertFalse(instance.equals(null));
		assertFalse(instance.equals(new Object()));
		for (int i = 0; i < propertiesAndDefaultValues.length; i += 2) {
			String field = (String) propertiesAndDefaultValues[i];
			Object value = propertiesAndDefaultValues[i + 1];
			Whitebox.setInternalState(instance, field, value);
		}
		T cloned = (T) instance.clone();
		for (int i = 0; i < propertiesAndDefaultValues.length; i += 2) {
			String field = (String) propertiesAndDefaultValues[i];
			Object value = propertiesAndDefaultValues[i + 1];
			assertEquals(Whitebox.getInternalState(cloned, field), value);
		}

	}

	public static <T> void testBuilders(Class<T> clazz,Set<String>exclusions) throws Exception {
		Map<Field, Object> propertiesAndNonDefaultValues = getpropertiesAndValues(clazz, exclusions, false,true);
		T instance=null;
		try{
			instance=clazz.newInstance();
		}catch(InstantiationException ex){
			instance=Whitebox.newInstance(clazz);
		}
		
		for (Field field:propertiesAndNonDefaultValues.keySet()) {	
			System.out.println("testing builders methods for field: "+field);
			Method[] methods = {};
			try {
				methods = PowerMockito.methods(instance.getClass(), new String[] { field.getName()});
			} catch (MethodNotFoundException ex) {
				continue;
			}
			for (Method method : methods) {
				if (method.getDeclaringClass() == instance.getClass()) {
					if (method.getParameterTypes().length == 1) {
						Object value = propertiesAndNonDefaultValues.get(field);
						if (value != null && method.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
							T res = Whitebox.invokeMethod(instance, field.getName(), value);
							assertEquals(Whitebox.getInternalState(res, field.getName()), value);
						}
					}
				}
			}

		}
	}

	public static <T> void testEquals(T instance, T other, Map<Field, Object> propertiesAndDefaultValues, Map<Field, Object> propertiesAndNonDefaultValues) {
		assertTrue(instance.equals(instance));
		assertFalse(instance.equals(null));
		assertFalse(instance.equals(new Object()));
		for (Field field:propertiesAndDefaultValues.keySet()) {
			System.out.println("testing field: "+field);
			for (Field otherField:propertiesAndDefaultValues.keySet()) {
				if(field.equals(otherField)) continue;
				//make suer instance and other have the same values for the other fields
				Whitebox.setInternalState(other, otherField.getName(), propertiesAndNonDefaultValues.get(otherField));
				Whitebox.setInternalState(instance, otherField.getName(), propertiesAndNonDefaultValues.get(otherField));
			}
			Object nonDefaultValue = propertiesAndNonDefaultValues.get(field);
			Whitebox.setInternalState(other, field.getName(), nonDefaultValue);
			Object defaultValue=propertiesAndDefaultValues.get(field);
			Whitebox.setInternalState(instance, field.getName(), defaultValue);			
			assertFalse(instance.equals(other));
			assertFalse(other.equals(instance));
			//test hashcode consistency
			assertEquals(instance.hashCode(), instance.hashCode());
			Whitebox.setInternalState(instance, field.getName(), nonDefaultValue);
		}
		assertTrue(instance.equals(other));
		assertEquals(instance.hashCode(), other.hashCode());
	}

	
	
    public static final Object getTypeDefaultValue(Class<?>  clazz) {
        if (clazz==byte.class)
            return 0;
        if (clazz==short.class)
            return 0;
        if (clazz==int.class)
            return 0;
        if (clazz==long.class)
            return 0L;
        if (clazz==char.class)
            return '\u0000';
        if (clazz==float.class)
            return 0.0F;
        if (clazz==double.class)
            return 0.0;
        if (clazz==boolean.class)
            return false;
        return null;
    }
    
    public static final Object getTypeNonDefaultValue(Class<?>  clazz) {
        if (clazz==byte.class)
            return 1;
        if (clazz==short.class)
            return 1;
        if (clazz==int.class)
            return 1;
        if (clazz==long.class)
            return 1L;
        if (clazz==char.class)
            return 'z';
        if (clazz==float.class)
            return 1.0F;
        if (clazz==double.class)
            return 1.0;
        if (clazz==boolean.class)
            return true;
        if(clazz==String.class){
        	return "non null string";
        }
        if(clazz.equals(List.class)){
        	return new ArrayList();
        }
        return PowerMockito.mock(clazz);
    }


}
