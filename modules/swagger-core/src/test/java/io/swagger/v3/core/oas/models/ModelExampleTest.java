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

package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ModelExampleTest {
    @Test(description = "it should create a model")
    public void createModel() {
        ObjectSchema model = new ObjectSchema();
        model.addProperties("name", new StringSchema().example("Tony"));
        model.addProperties("id", new IntegerSchema().example(123));
        assertNotNull(model);
    }

    @Test(description = "it should create a model with example")
    public void createModelWithExample() {
        ObjectSchema model = new ObjectSchema();

        model.addProperties("name", new StringSchema().example("Tony"));
        model.addProperties("id", new IntegerSchema().example(123));
        model.example("{\"name\":\"Fred\",\"id\":123456\"}");

        assertEquals(model.getExample(), "{\"name\":\"Fred\",\"id\":123456\"}");
    }
}
