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

package io.swagger.test;

import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SchemaTests {

    @Test
    public void AdditionalPropertiesBoolean(){
        Map<String, Schema> schemas = new HashMap<>();

        schemas.put("StringSchema", new StringSchema()
                        .description("simple string schema")
                        .minLength(3)
                        .maxLength(100)
                        .example("it works")
                        .additionalProperties(true)
                );
    }

    @Test
    public void AdditionalPropertiesSchema(){
        Map<String, Schema> schemas = new HashMap<>();

        schemas.put("IntegerSchema", new IntegerSchema()
                .description("simple integer schema")
                .multipleOf(new BigDecimal(3))
                .minimum(new BigDecimal(6))
                .additionalProperties(new StringSchema())
        );

    }

    @Test
    public void AdditionalPropertiesException()throws Exception{
        Map<String, Schema> schemas = new HashMap<>();
        try {
            schemas.put("IntegerSchema", new IntegerSchema()
                    .description("simple integer schema")
                    .multipleOf(new BigDecimal(3))
                    .minimum(new BigDecimal(6))
                    .additionalProperties("ok")
            );
            Assert.fail("Should have thrown an exception");
        }catch (Exception exception){
            
        }
    }
}
