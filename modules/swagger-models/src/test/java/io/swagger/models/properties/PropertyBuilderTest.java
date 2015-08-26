package io.swagger.models.properties;

import org.testng.Assert;

import org.testng.annotations.Test;

public class PropertyBuilderTest {

    @Test
    public void testStringPredefinedFormats() {
        for (final StringProperty.Format format : StringProperty.Format.values()) {
            final String name = format.getName();
            final Property property = PropertyBuilder.build(StringProperty.TYPE, name, null);
            Assert.assertEquals(property.getType(), StringProperty.TYPE);
            Assert.assertEquals(property.getFormat(), name);
        }
    }

    @Test
    public void testStringNoFormat() {
        final Property noFormat = PropertyBuilder.build(StringProperty.TYPE, null, null);
        Assert.assertEquals(noFormat.getType(), StringProperty.TYPE);
        Assert.assertNull(noFormat.getFormat());
    }

    @Test
    public void testStringCustomFormat() {
        final String customFormatName = "custom";
        final Property customFormatProperty = PropertyBuilder.build(StringProperty.TYPE, customFormatName, null);
        Assert.assertNotNull(customFormatProperty);
        Assert.assertEquals(customFormatProperty.getType(), StringProperty.TYPE);
        Assert.assertEquals(customFormatProperty.getFormat(), customFormatName);
    }

    @Test
    public void testNumberFloat() {
        final String floatFormat = "float";
        final Property floatProperty = PropertyBuilder.build(DecimalProperty.TYPE, floatFormat, null);
        Assert.assertEquals(floatProperty.getType(), DecimalProperty.TYPE);
        Assert.assertEquals(floatProperty.getFormat(), floatFormat);
    }

    @Test
    public void testNumberNoFormat() {
        final Property noFormat = PropertyBuilder.build(DecimalProperty.TYPE, null, null);
        Assert.assertEquals(noFormat.getType(), DecimalProperty.TYPE);
        Assert.assertNull(noFormat.getFormat());
    }

    @Test
    public void testNumberCustomFormat() {
        final String customFormatName = "custom";
        final Property customFormatProperty = PropertyBuilder.build(DecimalProperty.TYPE, customFormatName, null);
        Assert.assertNotNull(customFormatProperty);
        Assert.assertEquals(customFormatProperty.getType(), DecimalProperty.TYPE);
        Assert.assertEquals(customFormatProperty.getFormat(), customFormatName);
    }

}
