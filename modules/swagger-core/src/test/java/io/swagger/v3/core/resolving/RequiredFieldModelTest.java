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
import io.swagger.v3.core.oas.models.ApiFirstRequiredFieldModel;
import io.swagger.v3.core.oas.models.RequiredRefFieldModel;
import io.swagger.v3.core.oas.models.XmlFirstRequiredFieldModel;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class RequiredFieldModelTest {
    @Test(description = "it should apply required flag when ApiProperty annotation first")
    public void testApiModelPropertyFirstPosition() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ApiFirstRequiredFieldModel.class);
        final Schema model = models.get("aaa");
        final Schema prop = (Schema) model.getProperties().get("bla");
        assertNotNull(prop);
        assertTrue(model.getRequired().contains("bla"));
    }

    @Test(description = "it should apply required flag when XmlElement annotation first")
    public void testApiModelPropertySecondPosition() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(XmlFirstRequiredFieldModel.class);
        final Schema model = models.get("aaa");
        final Schema prop = (Schema) model.getProperties().get("a");
        assertNotNull(prop);
        assertTrue(model.getRequired().contains("a"));
    }


    @Test(description = "it should apply required flag also to ref fields")
    public void testApiModelRefProperty() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(RequiredRefFieldModel.class);
        final Schema model = models.get("RequiredRefFieldModel");
        final Schema prop = (Schema) model.getProperties().get("a");
        assertNotNull(prop);
        final Schema prop2 = (Schema) model.getProperties().get("b");
        assertNotNull(prop2);
        assertTrue(model.getRequired().contains("a"));
        assertTrue(model.getRequired().contains("b"));
    }
}
