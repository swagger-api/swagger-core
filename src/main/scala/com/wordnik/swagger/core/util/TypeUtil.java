/**
 *  Copyright 2011 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.core.util;

import com.wordnik.swagger.core.SwaggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

public class TypeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeUtil.class.getName());
    private static final String WORDNIK_PACKAGES = "com.wordnik.";
    private static final Map<String, Set<String>> REFERENCED_CLASSES_CACHE = new HashMap<String, Set<String>>();

    /**
     * @return true if the passed type represents a paramterized list
     */
    public static boolean isParameterizedList(Type genericType) {
        boolean isList = false; //List.class.isAssignableFrom(type.getClass()) || Collection.class.isAssignableFrom(type.getClass());
        final boolean isTypeParameterized = ParameterizedType.class.isAssignableFrom(genericType.getClass());
        if (isTypeParameterized) {
            final ParameterizedType parameterizedType = (ParameterizedType) genericType;
            isList = parameterizedType.getRawType().equals(List.class);
        }

        return isList && isTypeParameterized;
    }

    /**
     * @return true if the passed type represents a paramterized set
     */
    public static boolean isParameterizedSet(Type genericType) {
        boolean isSet = false; //List.class.isAssignableFrom(type.getClass()) || Collection.class.isAssignableFrom(type.getClass());
        final boolean isTypeParameterized = ParameterizedType.class.isAssignableFrom(genericType.getClass());
        if (isTypeParameterized) {
            final ParameterizedType parameterizedType = (ParameterizedType) genericType;
            isSet = parameterizedType.getRawType().equals(Set.class);
        }

        return isSet && isTypeParameterized;
    }

    /**
     * @return true if the passed type represents a paramterized map
     */
    public static boolean isParameterizedMap(Type genericType) {
        boolean isList = false; //List.class.isAssignableFrom(type.getClass()) || Collection.class.isAssignableFrom(type.getClass());
        final boolean isTypeParameterized = ParameterizedType.class.isAssignableFrom(genericType.getClass());
        if (isTypeParameterized) {
            final ParameterizedType parameterizedType = (ParameterizedType) genericType;
            isList = parameterizedType.getRawType().equals(Map.class);
        }

        return isList && isTypeParameterized;
    }

    /**
     * Gets a parameterized lists types if they are in com.wordnik.* packages
     */
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

    /**
     * Get all classes references by a given list of classes. This includes types of method params and fields
     */
    public static Collection<String> getReferencedClasses(List<String> classNameList) throws ClassNotFoundException {
        final Set<String> referencedClasses = new HashSet<String>();

        for (String className : classNameList) {
            referencedClasses.addAll(getReferencedClasses(className));
        }

        return referencedClasses;
    }

    /**
     * Get all classes references by a given class. This includes types of method params and fields
     */
    public static Collection<String> getReferencedClasses(String className) throws ClassNotFoundException {
        if (REFERENCED_CLASSES_CACHE.containsKey(className))
            return REFERENCED_CLASSES_CACHE.get(className);
        else {
            final Set<String> referencedClasses = new HashSet<String>();

            if (className.indexOf(".") > 0) {
                referencedClasses.add(className);

                Class clazz = null;
                try {
                    clazz = SwaggerContext.loadClass(className);
                } catch (Exception e) {
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
