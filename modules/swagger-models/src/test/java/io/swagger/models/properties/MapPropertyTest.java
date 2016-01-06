package io.swagger.models.properties;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MapPropertyTest {
    @Test
    public void testIsType() {
        assertTrue(MapProperty.isType("object", null), "isType must return true for 'object' and null");
    }

    @Test
    public void testAdditionalProperties() {
        // given
        MapProperty mapProperty = new MapProperty(new IntegerProperty());
        DoubleProperty property = new DoubleProperty();

        // when
        mapProperty.additionalProperties(property);

        // then
        assertEquals(mapProperty.getAdditionalProperties(), property,
                "The get additional properties must be the same as the set one");
    }
}
