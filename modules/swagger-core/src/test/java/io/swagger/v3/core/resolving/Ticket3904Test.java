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

package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket3904Test extends SwaggerTestBase {

    @Test
    public void testJsonValueSchemaAnnotation() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(Request.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), "Request:\n" +
                "  type: object\n" +
                "  properties:\n" +
                "    id:\n" +
                "      type: string\n" +
                "      description: Description of ID.");

    }

    static class Request {
        @io.swagger.v3.oas.annotations.media.Schema(description = "Description of ID.")
        private Id id;

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }
    }
    static class Id {
        private String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
