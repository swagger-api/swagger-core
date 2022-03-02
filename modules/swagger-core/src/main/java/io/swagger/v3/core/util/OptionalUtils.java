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

package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public abstract class OptionalUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(OptionalUtils.class);

    public static boolean _isOptionalType(JavaType jtype) {

        return Arrays.asList("com.google.common.base.Optional", "java.util.Optional")
                .contains(jtype.getRawClass().getCanonicalName());
    }

    /**
     * check if type is an Optional type, returns the unwrapped type in case, otherwise null
     *
     * @param type
     *
     */
    public static AnnotatedType unwrapOptional(AnnotatedType type) {

        if (type == null) {
            return type;
        }
        try {
            final JavaType jtype;
            if (type.getType() instanceof JavaType) {
                jtype = (JavaType) type.getType();
            } else {
                jtype = Json.mapper().constructType(type.getType());
            }

            if (_isOptionalType(jtype)) {
                AnnotatedType aType = new AnnotatedType()
                        .type(jtype.containedType(0))
                        .name(type.getName())
                        .parent(type.getParent())
                        .jsonUnwrappedHandler(type.getJsonUnwrappedHandler())
                        .skipOverride(type.isSkipOverride())
                        .schemaProperty(type.isSchemaProperty())
                        .ctxAnnotations(type.getCtxAnnotations())
                        .resolveAsRef(type.isResolveAsRef())
                        .jsonViewAnnotation(type.getJsonViewAnnotation())
                        .skipSchemaName(type.isSkipSchemaName())
                        .skipJsonIdentity(type.isSkipJsonIdentity())
                        .propertyName(type.getPropertyName());
                return aType;
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error unwrapping optional", e);
            return null;
        }
    }
}
