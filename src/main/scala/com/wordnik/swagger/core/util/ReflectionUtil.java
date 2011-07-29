package com.wordnik.swagger.core.util;

import java.lang.reflect.Method;

/**
 * @author ayush
 * @since 6/23/11 12:32 PM
 */
public class ReflectionUtil {

    /**
     * determines which interface declared a given method on a class
     */
    public static Class<?> getDeclaringInterface(final Method method) {
        for (Class<?> anInterface : method.getDeclaringClass().getInterfaces()) {
            final Method[] interfaceMethods = anInterface.getMethods();
            for (Method interfaceMethod : interfaceMethods) {
                if (interfaceMethod.getName().equals(method.getName()) && interfaceMethod.getReturnType().equals(method.getReturnType())) {
                    final Class<?>[] iMethodParams = interfaceMethod.getParameterTypes();
                    final Class<?>[] methodParams = method.getParameterTypes();

                    boolean equal = true;
                    if (iMethodParams.length == methodParams.length) {
                        for (int i = 0; i < methodParams.length; i++) {
                            final Class<?> methodParam = methodParams[i];
                            final Class<?> iMethodParam = iMethodParams[i];


                            if (!methodParam.equals(iMethodParam)) {
                                equal = false;
                                break;
                            }
                        }
                    }

                    if (equal) {
                        return interfaceMethod.getDeclaringClass();
                    }

                }

            }
        }
        return null;
    }
}
