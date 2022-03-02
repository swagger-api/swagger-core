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

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

public class Ticket2926Test extends SwaggerTestBase {

    @Test
    public void testExtensionsInMapDeserializeAndSerialize() throws Exception {
        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  title: My title\n" +
                "  description: API under test\n" +
                "  version: 1.0.7\n" +
                "  x-info: test\n" +
                "servers:\n" +
                "- url: http://localhost:9999/api\n" +
                "  x-server: test\n" +
                "  description: desc\n" +
                "  variables: \n" +
                "    serVar: \n" +
                "      description: desc\n" +
                "      x-serverVariable: test\n" +
                "paths:\n" +
                "  /foo/bar:\n" +
                "    get:\n" +
                "      callbacks:\n" +
                "        /foo/bar:\n" +
                "          get:\n" +
                "            description: getoperation\n" +
                "          x-callback: test\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: it works!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                title: inline_response_200\n" +
                "                type: object\n" +
                "                properties:\n" +
                "                  name:\n" +
                "                    type: string\n" +
                "              x-mediatype: test\n" +
                "          x-response: test\n" +
                "        x-responses: test\n" +
                "        x-responses-object: \n" +
                "          aaa: bbb\n" +
                "        x-responses-array: \n" +
                "          - aaa\n" +
                "          - bbb\n" +
                "      x-operation: test\n" +
                "    x-pathitem: test\n" +
                "  x-paths: test\n" +
                "x-openapi-object: \n" +
                "  aaa: bbb\n" +
                "x-openapi-array: \n" +
                "  - aaa\n" +
                "  - bbb\n" +
                "x-openapi: test";

        OpenAPI aa = Yaml.mapper().readValue(yaml, OpenAPI.class);
        SerializationMatchers.assertEqualsToYaml(aa, yaml);

    }

}
