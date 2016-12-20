package io.swagger.util;

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

import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ReflectionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    public static Type typeFromString(String type) {
        final PrimitiveType primitive = PrimitiveType.fromName(type);
        if (primitive != null) {
            return primitive.getKeyClass();
        }
        try {
            return loadClassByName(type);
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to resolve '%s' into class", type), e);
        }
        return null;
    }

    /**
     * Load Class by class name. If class not found in it's Class loader or one of the parent class loaders - delegate to the Thread's ContextClassLoader
     *
     * @param className Canonical class name
     * @return Class definition of className
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClassByName(String className) throws ClassNotFoundException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        }
    }

    /**
     * Checks if the method methodToFind is the overridden method from the superclass or superinterface.
     *
     * @param methodToFind is method to check
     * @param cls          is method class
     * @return true if the method is overridden method
     */
    public static boolean isOverriddenMethod(Method methodToFind, Class<?> cls) {
        Set<Class<?>> superClasses = Sets.newHashSet(cls.getInterfaces());

        if (cls.getSuperclass() != null) {
            superClasses.add(cls.getSuperclass());
        }

        for (Class<?> superClass : superClasses) {
            if (superClass != null && !(superClass.equals(Object.class))) {
                for (Method method : superClass.getMethods()) {
                    if (method.getName().equals(methodToFind.getName()) && method.getReturnType().isAssignableFrom(methodToFind.getReturnType())
                            && Arrays.equals(method.getParameterTypes(), methodToFind.getParameterTypes()) && !Arrays.equals(method.getGenericParameterTypes(), methodToFind.getGenericParameterTypes())) {
                        return true;
                    }
                }
                if (isOverriddenMethod(methodToFind, superClass)) {
                    return true;
                }
            }

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
        Method result = null;
        if (superClass != null && !(superClass.equals(Object.class))) {
            result = findMethod(method, superClass);
        }
        if (result == null) {
            for (Class<?> anInterface : declaringClass.getInterfaces()) {
                result = findMethod(method, anInterface);
                if (result != null) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * Searches the method methodToFind in given class cls. If the method is found returns it, else return null.
     *
     * @param methodToFind is the method to search
     * @param cls          is the class or interface where to search
     * @return method if it is found
     */
    public static Method findMethod(Method methodToFind, Class<?> cls) {
        if (cls == null) {
            return null;
        }
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
        if (cls == null || Object.class.equals(cls)) {
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
            for (Annotation metaAnnotation : method.getAnnotations()) {
                annotation = metaAnnotation.annotationType().getAnnotation(annotationClass);
                if (annotation != null) {
                    return annotation;
                }
            }
            Method superclassMethod = getOverriddenMethod(method);
            if (superclassMethod != null) {
                annotation = getAnnotation(superclassMethod, annotationClass);
            }
        }
        return annotation;
    }

    public static <A extends Annotation> A getAnnotation(Class<?> cls, Class<A> annotationClass) {
        A annotation = cls.getAnnotation(annotationClass);
        if (annotation == null) {
            for (Annotation metaAnnotation : cls.getAnnotations()) {
                annotation = metaAnnotation.annotationType().getAnnotation(annotationClass);
                if (annotation != null) {
                    return annotation;
                }
                ;
            }
            Class<?> superClass = cls.getSuperclass();
            if (superClass != null && !(superClass.equals(Object.class))) {
                annotation = getAnnotation(superClass, annotationClass);
            }
        }
        if (annotation == null) {
            for (Class<?> anInterface : cls.getInterfaces()) {
                for (Annotation metaAnnotation : anInterface.getAnnotations()) {
                    annotation = metaAnnotation.annotationType().getAnnotation(annotationClass);
                    if (annotation != null) {
                        return annotation;
                    }
                    ;
                }
                annotation = getAnnotation(anInterface, annotationClass);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        return annotation;
    }

    public static Annotation[][] getParameterAnnotations(Method method) {
        Annotation[][] methodAnnotations = method.getParameterAnnotations();
        Method overriddenmethod = getOverriddenMethod(method);

        if (overriddenmethod != null) {
            Annotation[][] overriddenAnnotations = overriddenmethod
                    .getParameterAnnotations();

            for (int i = 0; i < methodAnnotations.length; i++) {
                List<Type> types = new ArrayList<Type>();
                for (int j = 0; j < methodAnnotations[i].length; j++) {
                    types.add(methodAnnotations[i][j].annotationType());
                }
                for (int j = 0; j < overriddenAnnotations[i].length; j++) {
                    if (!types.contains(overriddenAnnotations[i][j]
                            .annotationType())) {
                        methodAnnotations[i] = ArrayUtils.add(
                                methodAnnotations[i],
                                overriddenAnnotations[i][j]);
                    }
                }

            }
        }
        return methodAnnotations;
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
