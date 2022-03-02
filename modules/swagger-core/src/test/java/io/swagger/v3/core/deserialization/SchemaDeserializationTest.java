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

import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class SchemaDeserializationTest {

    @Test
    public void deserializeRefSchema3_1() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1_refs_siblings.yaml");
        final OpenAPI openAPI = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(openAPI);
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSchemas());
        assertNotNull(openAPI.getComponents().getSchemas().get("AnotherPet"));
        assertEquals(openAPI.getComponents().getSchemas().get("AnotherPet").get$ref(), "#/components/schemas/Pet");
        assertEquals(openAPI.getComponents().getSchemas().get("AnotherPet").getTitle(), "Another Pet");
        assertEquals(openAPI.getComponents().getSchemas().get("AnotherPet").getDescription(), "Another Pet for petstore referencing Pet schema");
        assertNotNull(openAPI.getComponents().getSchemas().get("AnotherPet").getProperties());
        assertNotNull(openAPI.getComponents().getSchemas().get("AnotherPet").getProperties().get("category"));
        assertNotNull(openAPI.getComponents().getSchemas().get("AnotherPet").getProperties().get("photoUrl"));
    }
}
