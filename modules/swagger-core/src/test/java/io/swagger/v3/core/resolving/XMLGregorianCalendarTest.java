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
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class XMLGregorianCalendarTest {
    @Test(description = "it should read a model with XMLGregorianCalendar")
    public void testXMLGregorianCalendar() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithCalendar.class);
        assertEquals(models.size(), 1); // don't create a Joda DateTime object

        Schema model = models.get("ModelWithCalendar");

        final Map<String, Schema> properties = model.getProperties();

        final Schema nameProperty = properties.get("name");
        assertTrue(nameProperty instanceof StringSchema);
        assertEquals(nameProperty.getDescription(), "name of the model");

        final Schema dateTimeSchema = properties.get("createdAt");
        assertTrue(dateTimeSchema instanceof DateTimeSchema);
        assertTrue(model.getRequired().contains("createdAt"));
        assertEquals(dateTimeSchema.getDescription(), "creation timestamp");
    }

    class ModelWithCalendar {
        @io.swagger.v3.oas.annotations.media.Schema(description = "name of the model")
        public String name;

        @io.swagger.v3.oas.annotations.media.Schema(description = "creation timestamp", required = true)
        public XMLGregorianCalendar createdAt;
    }
}
