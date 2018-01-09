package io.swagger;

import io.swagger.models.Model;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.exceptions.FieldNotFoundException;
import org.powermock.reflect.exceptions.MethodNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class TestUtils {
    private final static Logger LOGGER = Logger.getLogger(TestUtils.class.getName());

    private TestUtils() {
    }

    private static <T> Map<Field, Object> getPropertiesAndValues(Class<T> clazz, Set<String> exclusions,
                                                                 boolean defaultValues, boolean includeInherited)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Map<Field, Object> propertiesAndDefaultValues = new HashMap<Field, Object>();
        Field[] fields = PowerMockito.fields(clazz);
        for (Field field : fields) {
            if (exclusions != null && exclusions.contains(field.getName()))
                continue;
            if (!includeInherited && field.getDeclaringClass() != clazz)
                continue;
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            Class<?> type = field.getType();
            Object value = null;
            if (defaultValues) {
                value = getTypeDefaultValue(type);
            } else {
                value = getTypeNonDefaultValue(type);
            }
            propertiesAndDefaultValues.put(field, value);
        }
        return propertiesAndDefaultValues;
    }

    private static boolean implementsMethod(Class<?> clazz, String methodName) {
        try {
            Method[] methods = PowerMockito.methods(clazz, new String[]{methodName});
            for (Method method : methods) {
                if (method.getDeclaringClass().equals(clazz))
                    return true;
            }
        } catch (MethodNotFoundException ex) {
            LOGGER.log(Level.INFO, ex.getMessage(), ex);
            return false;
        }
        return false;
    }

    public static <T> void testEquals(Class<T> clazz, Set<String> exclusions, boolean useInheritedFields)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        T instance = Whitebox.newInstance(clazz);

        T other = Whitebox.newInstance(clazz);
        Map<Field, Object> propertiesAndDefaultValues = getPropertiesAndValues(clazz, exclusions, true,
                useInheritedFields);
        Map<Field, Object> propertiesAndNonDefaultValues = getPropertiesAndValues(clazz, exclusions, false,
                useInheritedFields);
        testEquals(instance, other, propertiesAndDefaultValues, propertiesAndNonDefaultValues);
    }

    private static <T> Map<Field, Object> getPropertiesAndValues(Class<T> clazz, Set<String> exclusions,
                                                                 boolean defaultValues) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        return getPropertiesAndValues(clazz, exclusions, defaultValues, false);
    }

    public static <T extends Model> void testClone(T instance, Object[] propertiesAndDefaultValues) {
        for (int i = 0; i < propertiesAndDefaultValues.length; i += 2) {
            String field = (String) propertiesAndDefaultValues[i];
            Object value = propertiesAndDefaultValues[i + 1];
            Whitebox.setInternalState(instance, field, value);
        }
        T cloned = (T) instance.clone();
        for (int i = 0; i < propertiesAndDefaultValues.length; i += 2) {
            String field = (String) propertiesAndDefaultValues[i];
            Object value = propertiesAndDefaultValues[i + 1];
            assertEquals(Whitebox.getInternalState(cloned, field), value,
                    "the value of the clone and the cloned instances must be se same for field " + field);
        }

    }

    public static <T> void testBuilders(Class<T> clazz, Set<String> exclusions) throws Exception {
        Map<Field, Object> propertiesAndNonDefaultValues = getPropertiesAndValues(clazz, exclusions, false, true);
        T instance = null;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException ex) {
            instance = Whitebox.newInstance(clazz);
        }

        for (Field field : propertiesAndNonDefaultValues.keySet()) {
            Method[] methods = {};
            try {
                methods = PowerMockito.methods(instance.getClass(), new String[]{field.getName()});
            } catch (MethodNotFoundException ex) {
                continue;
            }
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1) {

                    Object value = propertiesAndNonDefaultValues.get(field);
                    if (value != null && method.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
                        T res = Whitebox.invokeMethod(instance, field.getName(), value);
                        assertEquals(Whitebox.getInternalState(res, field.getName()), value,
                                "the value of the field must be the one that has just be set:" + field);
                    }
                }
            }

        }
    }

    public static <T> void testCommonMethods(Class<T> clazz, Set<String> exclusions) throws Exception {
        T instance = null;

        Constructor<?>[] constructors = PowerMockito.constructorsDeclaredIn(clazz);
        for (Constructor<?> constructor : constructors) {
            Class<?>[] types = constructor.getParameterTypes();
            List<Object> parameters = new ArrayList<Object>();
            for (Class<?> type : types) {
                parameters.add(getTypeNonDefaultValue(type));
            }
            try {
                instance = Whitebox.invokeConstructor(clazz, types, parameters.toArray());
            } catch (Exception exx) {
                LOGGER.log(Level.INFO, exx.getMessage(), exx);
                continue;
            }
        }
        if (instance == null) {
            try {
                instance = clazz.newInstance();
            } catch (InstantiationException ex) {
                LOGGER.log(Level.INFO, ex.getMessage(), ex);

                instance = Whitebox.newInstance(clazz);
            }
        }
        Method[] methods = {};
        try {
            methods = PowerMockito.methodsDeclaredIn(instance.getClass());
        } catch (MethodNotFoundException ex) {
            LOGGER.log(Level.INFO, ex.getMessage(), ex);
        }
        Set<String> commonMethodNames = new HashSet<String>(
                Arrays.asList("_default", "_enum", "example", "vendorExtension", "setEnum"));

        for (Method method : methods) {
            if (commonMethodNames.contains(method.getName())
                    && (exclusions == null || !exclusions.contains(method.getName()))) {
                System.out.println("testing common method: " + method);
                List<Object> parameters = new ArrayList<Object>();
                Class<?>[] types = method.getParameterTypes();

                if (types.length <= 2) {
                    for (Class<?> type : types) {
                        parameters.add(getTypeNonDefaultValue(type));
                    }
                }
                if (method.getName().equals("vendorExtension")) {
                    parameters = Arrays.asList((Object) "x-vendor", "value");
                }
                Object[] parametersArray = parameters.toArray();

                String getterMethodName = method.getName();
                if (method.getName().startsWith("set")) {
                    getterMethodName = "g" + getterMethodName.substring(1);
                } else {
                    if (getterMethodName.startsWith("_"))
                        getterMethodName = getterMethodName.substring(1);
                    getterMethodName = "get" + getterMethodName.substring(0, 1).toUpperCase()
                            + getterMethodName.substring(1);
                    if (parameters.size() > 1)
                        getterMethodName = getterMethodName + "s";
                }

                Object value = parametersArray[0];
                boolean testWithUnknownString = false;
                if (parameters.size() == 1) {
                    Method getMethod = null;
                    try {
                        getMethod = PowerMockito.method(clazz, getterMethodName);
                    } catch (MethodNotFoundException ex) {
                        LOGGER.log(Level.INFO, ex.getMessage());
                        continue;
                    }
                    Class<?> retType = getMethod.getReturnType();
                    if (value instanceof String && !retType.isAssignableFrom(String.class)) {
                        value = getTypeNonDefaultValue(retType).toString();
                        parametersArray[0] = value;
                        testWithUnknownString = true;
                    }
                }

                try {
                    Whitebox.setInternalState(instance, "_enum", (Object) null);
                } catch (FieldNotFoundException ex) {
                    LOGGER.log(Level.INFO, ex.getMessage());
                }

                Whitebox.invokeMethod(instance, method.getName(), parametersArray);

                Object res = Whitebox.invokeMethod(instance, getterMethodName);

                if (parameters.size() > 1 && res instanceof Map) {
                    res = ((Map) res).get(parameters.get(0));
                    value = parameters.get(1);
                }
                if (value == null) {
                    assertNull(res, "the value returned by " + getterMethodName
                            + " must be null as we set it null through " + method);
                } else if (res instanceof Collection) {
                    assertTrue(((Collection) res).contains(value) || res.equals(value),
                            "the value returned by " + getterMethodName + " must contain the value set by " + method);
                } else {
                    assertEquals(res.toString(), value.toString(),
                            "the value returned by " + getterMethodName + " must be the same set by " + method);
                }

                if (testWithUnknownString) {
                    // try to raise an xxxformatexception
                    value = "unknown string";
                    parametersArray[0] = value;
                    Object actual = Whitebox.invokeMethod(instance, getterMethodName);
                    Whitebox.invokeMethod(instance, method.getName(), parametersArray);
                    res = Whitebox.invokeMethod(instance, getterMethodName);
                    assertEquals(actual, res, "the value must not change when passing an unknown value to " + method);
                }
            }
        }
    }

    public static <T> void testEquals(T instance, T other, Map<Field, Object> propertiesAndDefaultValues,
                                      Map<Field, Object> propertiesAndNonDefaultValues) {
        assertTrue(instance.equals(instance), "an instance must be equals to itself");
        assertFalse(instance.equals(null), "an instance must not be equals to null");
        assertFalse(instance.equals(new Object()), "an instance must not be equals to an object of another class");
        boolean equals;
        for (Field field : propertiesAndDefaultValues.keySet()) {
            // given
            for (Field otherField : propertiesAndDefaultValues.keySet()) {
                if (field.equals(otherField))
                    continue;
                // make suer instance and other have the same values for the
                // other fields
                Whitebox.setInternalState(other, otherField.getName(), propertiesAndNonDefaultValues.get(otherField));
                Whitebox.setInternalState(instance, otherField.getName(),
                        propertiesAndNonDefaultValues.get(otherField));
            }
            Object nonDefaultValue = propertiesAndNonDefaultValues.get(field);
            Whitebox.setInternalState(other, field.getName(), nonDefaultValue);
            Object defaultValue = propertiesAndDefaultValues.get(field);
            Whitebox.setInternalState(instance, field.getName(), defaultValue);

            // when
            equals = instance.equals(other);

            // then
            assertFalse(equals,
                    "an instance can not be equals to another if they don't have the same values for the involved fields:" + field);
            assertFalse(other.equals(instance),
                    "an instance can not be equals to another if they don't have the same values for the involved fields");

            // test hashcode consistency
            assertEquals(instance.hashCode(), instance.hashCode(), "hashcode must be consinstent between calls");
            Whitebox.setInternalState(instance, field.getName(), nonDefaultValue);
        }
        assertTrue(instance.equals(other), "if they have the same values, then the two instances must be equals: " + instance.getClass());
        assertEquals(instance.hashCode(), other.hashCode(),
                "if two instances are equals, then they must have the same hashcode");
    }

    public static final Object getTypeDefaultValue(Class<?> clazz) {
        if (clazz == byte.class)
            return 0;
        if (clazz == short.class)
            return 0;
        if (clazz == int.class)
            return 0;
        if (clazz == long.class)
            return 0L;
        if (clazz == char.class)
            return '\u0000';
        if (clazz == float.class)
            return 0.0F;
        if (clazz == double.class)
            return 0.0;
        if (clazz == boolean.class)
            return false;
        return null;
    }

    public static final <T> Object getTypeNonDefaultValue(Class<T> clazz) {
        if (clazz == byte.class)
            return 1;
        if (clazz == short.class)
            return 1;
        if (clazz == int.class)
            return 1;
        if (clazz == long.class)
            return 1L;
        if (clazz == char.class)
            return 'z';
        if (clazz == float.class)
            return 1.0F;
        if (clazz == double.class)
            return 1.0;
        if (clazz == boolean.class)
            return true;
        if (clazz == String.class) {
            return "non null string";
        }
        if (clazz.equals(List.class)) {
            return new ArrayList();
        }
        if (clazz.isEnum()) {
            try {
                T[] values = Whitebox.invokeMethod(clazz, "values");
                return values[0];
            } catch (Exception e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
        }
        return PowerMockito.mock(clazz);
    }

}
