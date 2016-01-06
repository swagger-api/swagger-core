package io.swagger.models.properties;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ObjectPropertyTest {

    ObjectProperty objectProperty;

    @BeforeMethod
    public void setup() {
        objectProperty = new ObjectProperty(null);
    }

    @Test
    public void testProperty() {
        // given
        ObjectProperty objectProperty = new ObjectProperty(null);

        DoubleProperty property = new DoubleProperty();
        String name = "name";

        // when
        objectProperty.property(name, property);

        // then
        assertEquals(objectProperty.getProperties().get(name), property,
                "The get property must be the same as the set one");
    }

    @Test
    public void testReadOnly() {
        // when
        objectProperty._default("default");
        objectProperty.readOnly(true);

        // then
        assertTrue(objectProperty.getReadOnly(), "The get readOnly must be the same as the set one");
    }

    @Test
    public void testRequired() {
        // when
        objectProperty._default("default");
        objectProperty.required(true);

        // then
        assertTrue(objectProperty.getRequired(), "The get required must be the same as the set one");
    }
}
