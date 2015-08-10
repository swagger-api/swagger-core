package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import io.swagger.models.ModelImpl;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.StringProperty;

import org.testng.annotations.Test;

public class ModelExampleTest {

    @Test(description = "it should create a model")
    public void createModel() {
        ModelImpl model = new ModelImpl()
                .property("name", new StringProperty().example("Tony"))
                .property("id", new LongProperty().example(123L));
        assertNotNull(model);
    }

    @Test(description = "it should create a model with example")
    public void createModelWithExample() {
        ModelImpl model = new ModelImpl()
                .property("name", new StringProperty().example("Tony"))
                .property("id", new LongProperty().example(123L))
                .example("{\"name\":\"Fred\",\"id\":123456\"}");

        assertEquals(model.getExample(), "{\"name\":\"Fred\",\"id\":123456\"}");
    }
}
