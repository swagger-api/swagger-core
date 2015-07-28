package io.swagger.jaxrs.utils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ReflectionUtils {

    private static final Set<Class<? extends Annotation>> CONSTRUCTOR_ANNOTATIONS;

    /**
     * Checks if the method methodToFind is the overridden method from the superclass.
     *
     * @param methodToFind is method to check
     * @param cls          is method class
     * @return true if the method is overridden method
     */
    public static boolean isOverriddenMethod(Method methodToFind, Class<?> cls) {
        Class<?> superClass = cls.getSuperclass();
        if (superClass != null && !(superClass.getClass().equals(Object.class))) {
            for (Method method : superClass.getMethods()) {
                if (!method.getName().equals(methodToFind.getName()) || !method.getReturnType().isAssignableFrom(methodToFind.getReturnType())) {
                    continue;
                }
                if (Arrays.equals(method.getParameterTypes(), methodToFind.getParameterTypes()) &&
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
        if (superClass != null && !(superClass.getClass().equals(Object.class))) {
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
        Class<?>[] pTypes = methodToFind.getParameterTypes();
        Type[] gpTypes = methodToFind.getGenericParameterTypes();
        methodLoop:
        for (Method method : cls.getMethods()) {
            if (
            	!method.getName().equals(methodToSearch) || 
            	!method.getReturnType().isAssignableFrom(methodToFind.getReturnType()) ||
            	method.getParameterTypes().length != pTypes.length
            	) {
                continue;
            }
            Class<?>[] pt = method.getParameterTypes();
            Type[] gpt = method.getGenericParameterTypes();
            for (int j = 0; j < pTypes.length; j++) {
                Class<?> parameterType = pTypes[j];
                if (!(pt[j].equals(parameterType) || (!gpt[j].equals(gpTypes[j]) && pt[j].isAssignableFrom(parameterType)))) {
                    continue methodLoop;
                }
            }
            return method;
        }
        return null;
    }

    /**
     * Searches for constructor suitable for resource instantiation.
     * <p/>
     * If more constructors exists the one with the most injectable parameters will be selected.
     *
     * @param cls is the class where to search
     * @return the suitable constructor
     */
    public static Constructor<?> findConstructor(Class<?> cls) {
        if (cls.isLocalClass() || (cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers()))) {
            return null;
        }

        Constructor<?> selected = null;
        int selectedCount = 0;
        int maxParams = -1;
        for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
            final Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length >= maxParams && isCompatible(constructor)) {
                if (parameterTypes.length > maxParams) {
                    maxParams = parameterTypes.length;
                    selectedCount = 0;
                }

                selected = constructor;
                selectedCount++;
            }
        }

        return selectedCount == 1 ? selected : null;
    }

    /**
     * Returns an implementation of {@link Function} for getting annotation types.
     *
     * @return the implementation of {@link Function} for getting annotation types
     */
    public static Function<Annotation, Class<? extends Annotation>> createAnnotationTypeGetter() {
        return new Function<Annotation, Class<? extends Annotation>>() {
            @Override
            public Class<? extends Annotation> apply(Annotation annotation) {
                return annotation.annotationType();
            }
        };
    }

    /**
     * Checks if the passed constructor is suitable for resource instantiation.
     * Repeats the logic of the {@link org.glassfish.jersey.internal.inject.JerseyClassAnalyzer#isCompatible(java.lang.reflect.Constructor)}
     *
     * @param constructor the constructor to be checked
     * @return true if the constructor is suitable or false otherwise
     */
    private static boolean isCompatible(Constructor<?> constructor) {
        for (Annotation annotation : constructor.getAnnotations()) {
            // use string name to avoid additional dependencies
            if ("javax.inject.Inject".equals(annotation.annotationType().getName())) {
                return true;
            }
        }

        if (!Modifier.isPublic(constructor.getModifiers())) {
            final int access = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;
            return constructor.getParameterTypes().length == 0 &&
                    (constructor.getDeclaringClass().getModifiers() & access) == constructor.getModifiers();
        }

        for (Annotation[] paramAnnotations : constructor.getParameterAnnotations()) {
            final Collection<Class<? extends Annotation>> tmp = Collections2.transform(Arrays.asList(paramAnnotations),
                    ReflectionUtils.createAnnotationTypeGetter());
            if (Collections.disjoint(tmp, CONSTRUCTOR_ANNOTATIONS)) {
                return false;
            }
        }

        return true;
    }

    static {
        final Set<Class<? extends Annotation>> constructorAnnotations = new HashSet<Class<? extends Annotation>>();
        constructorAnnotations.add(PathParam.class);
        constructorAnnotations.add(QueryParam.class);
        constructorAnnotations.add(HeaderParam.class);
        CONSTRUCTOR_ANNOTATIONS = Collections.unmodifiableSet(constructorAnnotations);
    }
}
