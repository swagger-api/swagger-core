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

package io.swagger.v3.core.deserialization.properties;

import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ArrayPropertyDeserializerTest {
    private static final String yaml =
            "      operationId: something\n" +
                    "      responses:\n" +
                    "        \"200\":\n" +
                    "          content:\n" +
                    "            '*/*':\n" +
                    "              examples:\n" +
                    "                simple:\n" +
                    "                  value: Array example\n" +
                    "                more:\n" +
                    "                  value: with two items\n" +
                    "              description: OK\n" +
                    "              schema:\n" +
                    "                type: array\n" +
                    "                minItems: 3\n" +
                    "                maxItems: 100\n" +
                    "                items:\n" +
                    "                  type: string\n";

    @Test(description = "it should includes the example in the arrayproperty")
    public void testArrayDeserialization() throws Exception {

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);

        MediaType media = response.getContent().get("*/*");
        Schema responseSchema = media.getSchema();
        assertTrue(media.getExamples().size() == 2);
        assertNotNull(responseSchema);
        assertTrue(responseSchema instanceof ArraySchema);

        ArraySchema mp = (ArraySchema) responseSchema;
        assertEquals(mp.getMinItems(), new Integer(3));
        assertEquals(mp.getMaxItems(), new Integer(100));
    }
}