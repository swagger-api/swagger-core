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

package io.swagger.v3.core.deserialization;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ObjectPropertyTest {
    @Test(description = "convert a model with object properties")
    public void readModelWithObjectProperty() throws IOException {
        String json = "{" +
                "   \"properties\":{" +
                "      \"id\":{" +
                "         \"type\":\"string\"" +
                "      }," +
                "      \"someObject\":{" +
                "         \"type\":\"object\"," +
                "        \"x-foo\": \"vendor x\"," +
                "         \"properties\":{" +
                "            \"innerId\":{" +
                "               \"type\":\"string\"" +
                "            }" +
                "         }" +
                "      }" +
                "   }" +
                "}";

        Schema model = Json.mapper().readValue(json, Schema.class);

        Schema p = (Schema) model.getProperties().get("someObject");
        assertTrue(p instanceof ObjectSchema);

        ObjectSchema op = (ObjectSchema) p;

        Schema sp = op.getProperties().get("innerId");
        assertTrue(sp instanceof StringSchema);

        assertTrue(op.getExtensions() != null);
        assertNotNull(op.getExtensions().get("x-foo"));
        assertEquals(op.getExtensions().get("x-foo"), "vendor x");
    }
}
