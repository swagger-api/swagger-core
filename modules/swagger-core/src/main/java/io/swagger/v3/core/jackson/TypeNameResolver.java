/**
 * Copyright 2021 SmartBear Software
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

package io.swagger.v3.core.jackson;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.core.util.PrimitiveType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Helper class used for converting well-known (property) types into
 * Swagger type names.
 */
public class TypeNameResolver {
    public static final TypeNameResolver std = new TypeNameResolver();
    private boolean useFqn=false;

    protected TypeNameResolver() {
    }

    public void setUseFqn(boolean useFqn) {
        this.useFqn = useFqn;
    }


    public String nameForType(JavaType type, Options... options) {
        return nameForType(type, options.length == 0 ? Collections.<Options>emptySet() :
                EnumSet.copyOf(Arrays.asList(options)));
    }

    public String nameForType(JavaType type, Set<Options> options) {
        if (type.hasGenericTypes()) {
            return nameForGenericType(type, options);
        }
        final String name = findStdName(type);
        return (name == null) ? nameForClass(type, options) : name;
    }

    protected String nameForClass(JavaType type, Set<Options> options) {
        return nameForClass(type.getRawClass(), options);
    }

    protected String nameForClass(Class<?> cls, Set<Options> options) {
        if (options.contains(Options.SKIP_API_MODEL)) {
            return getNameOfClass(cls);
        }

        io.swagger.v3.oas.annotations.media.Schema mp = AnnotationsUtils.getSchemaDeclaredAnnotation(cls);

        final String modelName = mp == null ? null : StringUtils.trimToNull(mp.name());
        return modelName == null ? getNameOfClass(cls) : modelName;
    }

    protected String getNameOfClass(Class<?> cls) {
        return useFqn?cls.getName():cls.getSimpleName();
    }

    protected String nameForGenericType(JavaType type, Set<Options> options) {
        final StringBuilder generic = new StringBuilder(nameForClass(type, options));
        final int count = type.containedTypeCount();
        for (int i = 0; i < count; ++i) {
            final JavaType arg = type.containedType(i);
            final String argName = PrimitiveType.fromType(arg) != null ? nameForClass(arg, options) :
                    nameForType(arg, options);
            generic.append(WordUtils.capitalize(argName));
        }
        return generic.toString();
    }

    protected String findStdName(JavaType type) {
        return PrimitiveType.getCommonName(type);
    }

    public enum Options {
        SKIP_API_MODEL;
    }
}
