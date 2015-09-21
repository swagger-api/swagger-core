package io.swagger.util;

import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    public static Type typeFromString(String type) {
        final PrimitiveType primitive = PrimitiveType.fromName(type);
        if (primitive != null) {
            return primitive.getKeyClass();
        }
        try {
            return Class.forName(type);
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to resolve '%s' into class", type), e);
        }
        return null;
    }

    /**
     * Checks if the method methodToFind is the overridden method from the superclass.
     *
     * @param methodToFind is method to check
     * @param cls          is method class
     * @return true if the method is overridden method
     */
    public static boolean isOverriddenMethod(Method methodToFind, Class<?> cls) {
        Class<?> superClass = cls.getSuperclass();
        if (superClass != null && !(superClass.equals(Object.class))) {
            for (Method method : superClass.getMethods()) {
                if (method.getName().equals(methodToFind.getName()) && method.getReturnType().isAssignableFrom(methodToFind.getReturnType())
                        && Arrays.equals(method.getParameterTypes(), methodToFind.getParameterTypes()) &&
                        !Arrays.equals(method.getGenericParameterTypes(), methodToFind.getGenericParameterTypes())) {
                    return true;
                }
            }
            return isOverriddenMethod(methodToFind, superClass);
        }
        return false;
    }

    /**
     * Returns overridden method from superclass if it exists. If method was not found returns null.
     *
     * @param method is method to find
     * @return overridden method from superclass
     */
    public static Method getOverriddenMethod(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        Class<?> superClass = declaringClass.getSuperclass();
        if (superClass != null && !(superClass.equals(Object.class))) {
            Method result = findMethod(method, superClass);
            if (result == null) {
                for (Class<?> anInterface : declaringClass.getInterfaces()) {
                    result = findMethod(method, anInterface);
                    if (result != null) {
                        return result;
                    }
                }
            } else {
                return result;
            }
        }
        return null;
    }

    /**
     * Searches the method methodToFind in given class cls. If the method is found returns it, else return null.
     *
     * @param methodToFind is the method to search
     * @param cls          is the class or interface where to search
     * @return method if it is found
     */
    public static Method findMethod(Method methodToFind, Class<?> cls) {
        String methodToSearch = methodToFind.getName();
        Class<?>[] soughtForParameterType = methodToFind.getParameterTypes();
        Type[] soughtForGenericParameterType = methodToFind.getGenericParameterTypes();
        for (Method method : cls.getMethods()) {
            if (method.getName().equals(methodToSearch) && method.getReturnType().isAssignableFrom(methodToFind.getReturnType())) {
                Class<?>[] srcParameterTypes = method.getParameterTypes();
                Type[] srcGenericParameterTypes = method.getGenericParameterTypes();
                if (soughtForParameterType.length == srcParameterTypes.length &&
                        soughtForGenericParameterType.length == srcGenericParameterTypes.length) {
                    if (hasIdenticalParameters(srcParameterTypes, soughtForParameterType, srcGenericParameterTypes, soughtForGenericParameterType)) {
                        return method;
                    }
                }
            }
        }
        return null;
    }

    private static boolean hasIdenticalParameters(Class<?>[] srcParameterTypes, Class<?>[] soughtForParameterType,
            Type[] srcGenericParameterTypes, Type[] soughtForGenericParameterType) {
        for (int j = 0; j < soughtForParameterType.length; j++) {
            Class<?> parameterType = soughtForParameterType[j];
            if (!(srcParameterTypes[j].equals(parameterType) || (!srcGenericParameterTypes[j].equals(soughtForGenericParameterType[j]) &&
                    srcParameterTypes[j].isAssignableFrom(parameterType)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInject(List<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            // use string name to avoid additional dependencies
            if ("javax.inject.Inject".equals(annotation.annotationType().getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConstructorCompatible(Constructor<?> constructor) {
        if (!Modifier.isPublic(constructor.getModifiers())) {
            final int access = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;
            return constructor.getParameterTypes().length == 0 &&
                    (constructor.getDeclaringClass().getModifiers() & access) == constructor.getModifiers();
        }
        return true;
    }

    /**
     * Returns the list of declared fields from the class <code>cls</code> and its superclasses
     * excluding <code>Object</code> class. If the field from child class hides the field from superclass,
     * the field from superclass won't be added to the result list.
     *
     * @param cls is the processing class
     * @return list of Fields
     */
    public static List<Field> getDeclaredFields(Class<?> cls) {
        if (cls.equals(Object.class)) {
            return Collections.emptyList();
        }
        final List<Field> fields = new ArrayList<Field>();
        final Set<String> fieldNames = new HashSet<String>();
        for (Field field : cls.getDeclaredFields()) {
            fields.add(field);
            fieldNames.add(field.getName());
        }
        for (Field field : getDeclaredFields(cls.getSuperclass())) {
            if (!fieldNames.contains(field.getName())) {
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * Returns an annotation by type from a method.
     *
     * @param method          is the method to find
     * @param annotationClass is the type of annotation
     * @param <A>             is the type of annotation
     * @return annotation if it is found
     */
    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationClass) {
        A annotation = method.getAnnotation(annotationClass);
        if (annotation == null) {
            Method superclassMethod = getOverriddenMethod(method);
            if (superclassMethod != null) {
                annotation = getAnnotation(superclassMethod, annotationClass);
            }
        }
        return annotation;
    }

    /**
     * Checks if the type is void.
     *
     * @param type is the type to check
     * @return true if the type is void
     */
    public static boolean isVoid(Type type) {
        final Class<?> cls = TypeFactory.defaultInstance().constructType(type).getRawClass();
        return Void.class.isAssignableFrom(cls) || Void.TYPE.isAssignableFrom(cls);
    }
}
