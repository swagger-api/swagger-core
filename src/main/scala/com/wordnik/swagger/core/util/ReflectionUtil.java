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

import java.lang.reflect.Method;

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
