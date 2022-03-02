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

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.JacksonUnwrappedRequiredProperty;
import org.testng.annotations.Test;

public class JacksonJsonUnwrappedTest {

  @Test(description = "test the @JsonUnwrapped behaviour when required Properties")
  public void jacksonJsonUnwrappedTest() {

    SerializationMatchers
        .assertEqualsToYaml(ModelConverters.getInstance().read(
            JacksonUnwrappedRequiredProperty.class), "InnerTypeRequired:\n" +
            "  required:\n" +
            "  - name\n" +
            "  type: object\n" +
            "  properties:\n" +
            "    foo:\n" +
            "      type: integer\n" +
            "      format: int32\n" +
            "    name:\n" +
            "      type: string\n" +
            "JacksonUnwrappedRequiredProperty:\n" +
            "  required:\n" +
            "  - name\n" +
            "  type: object\n" +
            "  properties:\n" +
                "    foo:\n" +
                "      type: integer\n" +
                "      format: int32\n" +
                "    name:\n" +
                "      type: string\n");
    }
}
