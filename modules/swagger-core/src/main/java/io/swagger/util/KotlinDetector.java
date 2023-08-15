/**
 * Copyright 2016 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.util;

import java.lang.annotation.Annotation;

public class KotlinDetector {
    private static final Boolean kotlinAvailable;
    private static final Class<? extends Annotation> kotlinDeprecated;

    static {
        kotlinAvailable = loadByClassOrNull("kotlin.Metadata") != null;
        kotlinDeprecated = loadByClassOrNull("kotlin.Deprecated");
    }

    private static <T> Class<T> loadByClassOrNull(String className) {
        try {
            return (Class<T>) ReflectionUtils.loadClassByName(className);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static boolean isKotlinPresent() {
        return kotlinAvailable;
    }

    public static Class<? extends Annotation> getKotlinDeprecated() {
        return kotlinDeprecated;
    }
}
