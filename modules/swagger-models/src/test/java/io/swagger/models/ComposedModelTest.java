package io.swagger.models;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class ComposedModelTest {

    private ComposedModel instance;

    @BeforeMethod
    public void setUp() throws Exception {
        instance = new ComposedModel();
    }

    @Test
    public void testGetProperties() {
        //when
        instance.setProperties(null);

        //then
        assertNull(instance.getProperties(), "Properties must be null as we have set them to null");
    }

    @Test
    public void testClone() {
        //when
        instance.setDescription("description");
        ComposedModel cloned = (ComposedModel) instance.clone();

        //then
        assertEquals(cloned.getDescription(), instance.getDescription(), "The clone must have the same description as the cloned instance");
    }
}
