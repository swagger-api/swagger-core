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

package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.oas.models.composition.AbstractBaseModelWithoutFields;
import io.swagger.v3.core.oas.models.composition.Animal;
import io.swagger.v3.core.oas.models.composition.AnimalClass;
import io.swagger.v3.core.oas.models.composition.AnimalWithSchemaSubtypes;
import io.swagger.v3.core.oas.models.composition.Human;
import io.swagger.v3.core.oas.models.composition.ModelWithFieldWithSubTypes;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class CompositionTest {

    @Test(description = "read a model with required params and description")
    public void readModelWithRequiredParams() throws IOException {
        compareAsJson(Human.class, "Human.json");
    }

    @Test(description = "read a model with composition")
    public void readModelWithComposition() throws IOException {
        compareAsJson(Animal.class, "Animal.json");
    }

    @Test(description = "read a model with composition")
    public void readModeWithSchemalWithComposition() throws IOException {
        compareAsJson(AnimalWithSchemaSubtypes.class, "AnimalWithSchemaSubtypes.json");
    }

    @Test(description = "read a model with composition")
    public void readClassModelWithComposition() throws IOException {
        compareAsJson(AnimalClass.class, "AnimalClass.json");
    }

    @Test(description = "create a model")
    public void createModel() throws IOException {
        compareAsJson(AbstractBaseModelWithoutFields.class, "AbstractBaseModelWithoutFields.json");
    }

    @Test(description = "create a ModelWithFieldWithSubTypes")
    public void createModelWithFieldWithSubTypes() throws IOException {
        compareAsJson(ModelWithFieldWithSubTypes.class, "ModelWithFieldWithSubTypes.json");
    }

    private void compareAsJson(Class<?> cls, String fileName) throws IOException {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(cls);
        Json.prettyPrint(schemas);
        final String json = ResourceUtils.loadClassResource(getClass(), fileName);
        SerializationMatchers.assertEqualsToJson(schemas, json);
    }
}
