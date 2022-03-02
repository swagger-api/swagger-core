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
import io.swagger.v3.core.oas.models.ModelWithJaxBDefaultValues;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class JaxBDefaultValueTest {

    @Test(description = "convert a model with Guava optionals")
    public void convertModelWithGuavaOptionals() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().read(ModelWithJaxBDefaultValues.class);
        final Map<String, Schema> properties = schemas.get("ModelWithJaxBDefaultValues").getProperties();
        assertEquals(properties.size(), 2);
        assertEquals(((StringSchema) properties.get("name")).getDefault(), "Tony");
        assertEquals((int) ((IntegerSchema) properties.get("age")).getDefault(), 100);
    }
}
