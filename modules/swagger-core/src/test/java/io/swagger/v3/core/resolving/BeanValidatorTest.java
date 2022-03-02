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
import io.swagger.v3.core.oas.models.BeanValidationsModel;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

public class BeanValidatorTest {

    @Test(description = "read bean validations")
    public void readBeanValidatorTest() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().readAll(BeanValidationsModel.class);
        final Schema model = schemas.get("BeanValidationsModel");
        final Map<String, Schema> properties = model.getProperties();

        Assert.assertTrue(model.getRequired().contains("id"));

        final IntegerSchema age = (IntegerSchema) properties.get("age");
        Assert.assertEquals(age.getMinimum(), new BigDecimal(13.0));
        Assert.assertEquals(age.getMaximum(), new BigDecimal(99.0));

        final StringSchema password = (StringSchema) properties.get("password");
        Assert.assertEquals((int) password.getMinLength(), 6);
        Assert.assertEquals((int) password.getMaxLength(), 20);

        final StringSchema email = (StringSchema) properties.get("email");
        Assert.assertEquals((String) email.getPattern(), "(.+?)@(.+?)");

        final NumberSchema minBalance = (NumberSchema) properties.get("minBalance");
        Assert.assertTrue(minBalance.getExclusiveMinimum());

        final NumberSchema maxBalance = (NumberSchema) properties.get("maxBalance");
        Assert.assertTrue(maxBalance.getExclusiveMaximum());

        final ArraySchema items = (ArraySchema) properties.get("items");
        Assert.assertEquals((int) items.getMinItems(), 2);
        Assert.assertEquals((int) items.getMaxItems(), 10);
    }
}
