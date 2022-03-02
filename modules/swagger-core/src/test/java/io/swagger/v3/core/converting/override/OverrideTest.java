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

package io.swagger.v3.core.converting.override;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converting.override.resources.GenericModel;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class OverrideTest {
    private static final String NAME = "name";
    private static final String COUNT = "count";

    @Test
    public void test() {
        GenericModel.declareProperty(NAME, String.class);
        GenericModel.declareProperty(COUNT, int.class);

        // create new instead of use singleton
        final ModelConverters converters = new ModelConverters();
        converters.addConverter(new GericModelConverter());
        final Map<String, Schema> read = converters.read(GenericModel.class);
        assertTrue(read.containsKey(GenericModel.class.getSimpleName()));

        final Schema model = read.get(GenericModel.class.getSimpleName());
        assertTrue(model.getProperties().containsKey(NAME));
        assertEquals(((Schema) model.getProperties().get(NAME)).getType(), "string");
        assertTrue(model.getProperties().containsKey(COUNT));
        assertEquals(((Schema) model.getProperties().get(COUNT)).getType(), "integer");
    }
}
