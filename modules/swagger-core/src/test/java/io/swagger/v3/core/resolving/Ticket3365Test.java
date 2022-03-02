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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class Ticket3365Test extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;

    @BeforeMethod
    public void beforeMethod() {
        modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }

    @Test
    public void testTicket3365() {
        Schema model = context.resolve(new AnnotatedType(ExampleWithJson.class));
        assertNotNull(model);
        SerializationMatchers.assertEqualsToYaml(model, "required:\n" +
                "- param1\n" +
                "type: object\n" +
                "properties:\n" +
                "  param1:\n" +
                "    title: Cron formatted invoice schedule\n" +
                "    type: object\n" +
                "    example:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: string\n" +
                "        format: byte");

        model = context.resolve(new AnnotatedType(ExampleStartingWithInteger.class));
        assertNotNull(model);
        SerializationMatchers.assertEqualsToYaml(model, "required:\n" +
                "- param1\n" +
                "type: object\n" +
                "properties:\n" +
                "  param1:\n" +
                "    title: Cron formatted invoice schedule\n" +
                "    type: string\n" +
                "    example: 0 0 5 * *\n");
    }

    static class ExampleStartingWithInteger  {
        @io.swagger.v3.oas.annotations.media.Schema(
                required = true,
                title = "Cron formatted invoice schedule",
                example = "0 0 5 * *"
        )
        public String param1;
    }
    static class ExampleWithJson  {
        @io.swagger.v3.oas.annotations.media.Schema(
                required = true,
                title = "Cron formatted invoice schedule",
                example = "{\"type\": \"array\", \"items\": {\"type\" : \"string\", \"format\": \"byte\"}}"
        )
        public Object param1;
    }
}
