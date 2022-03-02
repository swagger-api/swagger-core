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

package io.swagger.v3.core.serialization;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class SchemaSerializationTest {

    @Test
    public void serializeRefSchema3_1() {
        OpenAPI openAPI = new OpenAPI()
                .components(new Components()
                        .addSchemas("Pet", new Schema()
                                .addProperties("id", new Schema().type("integer"))
                                .addProperties("name", new Schema().type("string"))
                                .addProperties("tag", new Schema().type("string")))
                        .addSchemas("AnotherPet", new Schema()
                                .title("Another Pet")
                                .description("Another Pet for petstore referencing Pet schema")
                                .$ref("#/components/schemas/Pet")
                                .addProperties("category", new Schema().type("string"))
                                .addProperties("photoUrl", new Schema().type("string"))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, "openapi: 3.0.1\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Pet:\n" +
                "      properties:\n" +
                "        id: {}\n" +
                "        name: {}\n" +
                "        tag: {}\n" +
                "    AnotherPet:\n" +
                "      title: Another Pet\n" +
                "      properties:\n" +
                "        category: {}\n" +
                "        photoUrl: {}\n" +
                "      description: Another Pet for petstore referencing Pet schema\n" +
                "      $ref: '#/components/schemas/Pet'");
        SerializationMatchers.assertEqualsToYaml(openAPI, "openapi: 3.0.1\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Pet:\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "        name:\n" +
                "          type: string\n" +
                "        tag:\n" +
                "          type: string\n" +
                "    AnotherPet:\n" +
                "      $ref: '#/components/schemas/Pet'");
    }
}
