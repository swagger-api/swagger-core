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
