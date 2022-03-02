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
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.testng.annotations.Test;

import java.io.IOException;

public class ResponseExamplesTest {
    @Test(description = "it should create a response")
    public void createResponse() throws IOException {
        final ApiResponse response = new ApiResponse()
                .content(new Content()
                        .addMediaType("application/json", new MediaType()
                                .addExamples("test", new Example().value("{\"name\":\"Fred\",\"id\":123456\"}"))));

        final String json = "{\n" +
                "  \"content\" : {\n" +
                "    \"application/json\" : {\n" +
                "      \"examples\" : {\n" +
                "        \"test\" : {\n" +
                "          \"value\" : \"{\\\"name\\\":\\\"Fred\\\",\\\"id\\\":123456\\\"}\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(response, json);
    }
}
