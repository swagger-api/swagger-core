package com.wordnik.swagger.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author ayush
 * @since 6/23/11 12:38 PM
 */
public class TypeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeUtil.class.getName());
    private static final String WORDNIK_PACKAGES = "com.wordnik.";
    private static final Map<String, Set<String>> REFERENCED_CLASSES_CACHE = new HashMap<String, Set<String>>();

    public static boolean isParameterizedList(Type genericType) {
        boolean isList = false; //List.class.isAssignableFrom(type.getClass()) || Collection.class.isAssignableFrom(type.getClass());
        final boolean isTypeParameterized = ParameterizedType.class.isAssignableFrom(genericType.getClass());
        if (isTypeParameterized) {
            final ParameterizedType parameterizedType = (ParameterizedType) genericType;
            isList = parameterizedType.getRawType().equals(List.class);
        }

        return isList && isTypeParameterized;
    }

    public static boolean isParameterizedSet(Type genericType) {
        boolean isSet = false; //List.class.isAssignableFrom(type.getClass()) || Collection.class.isAssignableFrom(type.getClass());
        final boolean isTypeParameterized = ParameterizedType.class.isAssignableFrom(genericType.getClass());
        if (isTypeParameterized) {
            final ParameterizedType parameterizedType = (ParameterizedType) genericType;
            isSet = parameterizedType.getRawType().equals(Set.class);
        }

        return isSet && isTypeParameterized;
    }

    public static boolean isParameterizedMap(Type genericType) {
        boolean isList = false; //List.class.isAssignableFrom(type.getClass()) || Collection.class.isAssignableFrom(type.getClass());
        final boolean isTypeParameterized = ParameterizedType.class.isAssignableFrom(genericType.getClass());
        if (isTypeParameterized) {
            final ParameterizedType parameterizedType = (ParameterizedType) genericType;
            isList = parameterizedType.getRawType().equals(Map.class);
        }

        return isList && isTypeParameterized;
    }

    private static List<String> getWordnikParameterTypes(Type genericType) {
        List<String> list = new ArrayList<String>();

        if (isParameterizedList(genericType)) {
            final ParameterizedType parameterizedType = (ParameterizedType) genericType;
            for (Type _listType : parameterizedType.getActualTypeArguments()) {
                final Class listType = (Class) _listType;
                if (listType.getName().startsWith(WORDNIK_PACKAGES))
                    list.add(listType.getName());
            }
        }

        return list;
    }

    public static Collection<String> getReferencedClasses(List<String> classNameList) throws ClassNotFoundException {
        final Set<String> referencedClasses = new HashSet<String>();

        for (String className : classNameList) {
            referencedClasses.addAll(getReferencedClasses(className));
        }

        return referencedClasses;
    }

    public static Collection<String> getReferencedClasses(String className) throws ClassNotFoundException {
        if (REFERENCED_CLASSES_CACHE.containsKey(className))
            return REFERENCED_CLASSES_CACHE.get(className);
        else {
            final Set<String> referencedClasses = new HashSet<String>();

            if (className.indexOf(".") > 0) {
                referencedClasses.add(className);

                Class clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    LOGGER.error("Unable to load class " + className);
                }

                if (clazz != null) {
                    for (Field field : clazz.getDeclaredFields()) {
                        if (Modifier.isPublic(field.getModifiers())) {
                            String fieldClass = field.getType().getName();
                            if (fieldClass.startsWith(WORDNIK_PACKAGES)) {
                                referencedClasses.add(fieldClass);
                            } else {
                                referencedClasses.addAll(getWordnikParameterTypes(field.getGenericType()));
                            }
                        }
                    }

                    for (Method method : clazz.getDeclaredMethods()) {
                        if (Modifier.isPublic(method.getModifiers())) {
                            String methodReturnClass = method.getReturnType().getName();
                            if (methodReturnClass.startsWith(WORDNIK_PACKAGES)) {
                                referencedClasses.add(methodReturnClass);
                            } else {
                                referencedClasses.addAll(getWordnikParameterTypes(method.getGenericReturnType()));
                            }
                        }
                    }
                }
            }

            REFERENCED_CLASSES_CACHE.put(className, referencedClasses);

            final Set<String> additionalClasses = new HashSet<String>();
            for (String referencedClass : referencedClasses) {
                if (!REFERENCED_CLASSES_CACHE.containsKey(referencedClass)) {
                    additionalClasses.addAll(getReferencedClasses(referencedClass));
                }
            }
            referencedClasses.addAll(additionalClasses);

            return referencedClasses;
        }
    }
}
