package io.swagger.models.properties;

import io.swagger.models.properties.Property;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.swagger.models.properties.StringProperty;
import io.swagger.models.properties.PropertyBuilder;


public class PropertyBuilderTest {

    @Test
    public void testStringFormat() {
        for (StringProperty.Format format : StringProperty.Format.values()) {
            final String name = format.getName();
            final Property property = PropertyBuilder.build(StringProperty.TYPE, name, null);
            Assert.assertEquals(property.getType(), StringProperty.TYPE);
            Assert.assertEquals(property.getFormat(), name);
        }
        final Property noFormat = PropertyBuilder.build(StringProperty.TYPE, null, null);
        Assert.assertEquals(noFormat.getType(), StringProperty.TYPE);
        Assert.assertNull(noFormat.getFormat());
    }
}
