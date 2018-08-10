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
