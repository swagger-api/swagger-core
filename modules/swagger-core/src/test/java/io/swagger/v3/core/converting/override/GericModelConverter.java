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

package io.swagger.v3.core.converting.override;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.converting.override.resources.GenericModel;
import io.swagger.v3.core.jackson.AbstractModelConverter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Iterator;
import java.util.Map.Entry;

public class GericModelConverter extends AbstractModelConverter {

    protected GericModelConverter() {
        super(Json.mapper());
    }

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> next) {
        if (type.getType() instanceof Class<?>) {
            Class<?> cls = (Class<?>) type.getType();
            if (GenericModel.class.isAssignableFrom(cls)) {
                Schema impl = new Schema();
                impl.title(cls.getSimpleName());
                for (Entry<String, Class<?>> entry : GenericModel.getDeclaredProperties().entrySet()) {
                    impl.addProperties(entry.getKey(), context.resolve(new AnnotatedType(entry.getValue())));
                }
                context.defineModel(impl.getTitle(), impl);
                return impl;
            }
        }
        if (next.hasNext()) {
            return next.next().resolve(type, context, next);
        } else {
            return null;
        }
    }
}