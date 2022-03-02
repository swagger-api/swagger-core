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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;

import static org.testng.Assert.assertNotNull;

public class Ticket2189Test extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;

    @BeforeTest
    public void setup() {
        modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }

    @Test
    public void testTicket2189() {
        final Schema model = context.resolve(new AnnotatedType(BaseClass.class));
        assertNotNull(model);
        String yaml = "BaseClass:\n" +
                      "  type: object\n" +
                      "  properties:\n" +
                      "    property:\n" +
                      "      type: string\n" +
                      "    type:\n" +
                      "      type: string\n" +
                      "SubClass:\n" +
                      "  type: object\n" +
                      "  allOf:\n" +
                      "  - $ref: '#/components/schemas/BaseClass'\n" +
                      "  - type: object\n" +
                      "    properties:\n" +
                      "      subClassProperty:\n" +
                      "        type: string";

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), yaml);
    }

    static class SubClass extends BaseClass {
        public String subClassProperty;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
        @JsonSubTypes.Type(value = SubClass.class, name = "SubClass"),
        @JsonSubTypes.Type(value = BaseClass.class, name = "BaseClass"),
    })
    static class BaseClass {
        public String property;
        public String type;
    }
}
