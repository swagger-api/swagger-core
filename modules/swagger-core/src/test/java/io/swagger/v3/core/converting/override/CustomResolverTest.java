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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CustomResolverTest {

    @Test(description = "it should ignore properties with type Bar")
    public void testCustomConverter() {
        // add the custom converter
        final ModelConverters converters = new ModelConverters();
        converters.addConverter(new CustomConverter(Json.mapper()));

        Map<String, Schema> models = converters.readAll(Foo.class);
        Schema model = models.get("io.swagger.v3.core.converting.override.CustomResolverTest$Foo");
        assertNotNull(model);
        assertEquals(model.getProperties().size(), 2);

        final Schema barProperty = (Schema) model.getProperties().get("bar");
        assertEquals(barProperty.get$ref(), "#/components/schemas/io.swagger.v3.core.converting.override.CustomResolverTest$Bar");

        final Schema titleProperty = (Schema)model.getProperties().get("title");
        assertNotNull(titleProperty);

        model = models.get("io.swagger.v3.core.converting.override.CustomResolverTest$Bar");
        assertNotNull(model);

    }

    class CustomConverter extends ModelResolver {

        public CustomConverter(ObjectMapper mapper) {
            super(mapper, new QualifiedTypeNameResolver());
        }
    }

    class QualifiedTypeNameResolver extends TypeNameResolver {

        @Override
        protected String nameForClass(Class<?> cls, Set<Options> options) {
            String className = cls.getName().startsWith("java.") ? cls.getSimpleName() : cls.getName();
            if (options.contains(Options.SKIP_API_MODEL)) {
                return className;
            }
            final io.swagger.v3.oas.annotations.media.Schema model = cls.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
            final String modelName = model == null ? null : StringUtils.trimToNull(model.name());
            return modelName == null ? className : modelName;
        }
    }

    class Foo {
        public Bar bar = null;
        public String title = null;
    }

    class Bar {
        public String foo = null;
    }
}
