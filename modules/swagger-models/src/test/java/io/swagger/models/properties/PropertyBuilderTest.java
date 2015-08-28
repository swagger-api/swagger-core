package io.swagger.models.properties;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

import org.testng.Assert;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.swagger.models.properties.PropertyBuilder.PropertyId;
import io.swagger.models.properties.StringProperty.Format;

public class PropertyBuilderTest {

    private static final String STRING_FORMATS = "stringFormats";
    private static final String FROM_SPEC = "fromSpec";
    private static final String BY_IMPLEMENTATION = "predefined";
    private static final String CUSTOM_OR_PLAIN = "customOrPlain";

    @DataProvider(name = BY_IMPLEMENTATION)
    public Iterator<Object[]> createPredefinedProperties() {
        Property[] properties = {
            new DecimalProperty(), new FloatProperty(), new DoubleProperty(), new BaseIntegerProperty(),
            new IntegerProperty(), new LongProperty(), new StringProperty(), new UUIDProperty(), new BooleanProperty(),
            new ByteArrayProperty(), new ArrayProperty(), new ObjectProperty(), new DateTimeProperty(),
            new DateProperty(), new RefProperty(), new EmailProperty(),
            // new MapProperty() // MapProperty can't be distinguished from ObjectProperty
        };
        List<Object[]> resultList = new ArrayList<Object[]>(properties.length);
        for (Property property : properties) {
            resultList.add(new Object[] {property.getType(), property.getFormat(), property.getClass()});
        }

        return resultList.iterator();
    }

    @DataProvider(name = STRING_FORMATS)
    public Iterator<Object[]> createPredefinedStringFormats() {
        List<Object[]> resultList = new ArrayList<Object[]>();
        for (Format format : StringProperty.Format.values()) {
            resultList.add(new Object[] {StringProperty.TYPE, format.getName(), StringProperty.class});
        }

        return resultList.iterator();
    }

    @DataProvider(name = FROM_SPEC)
    public Object[][] createDataFromSpec() {

        // from the table in http://swagger.io/specification/#dataTypeType
        return new Object[][] {
                {"integer", "int32", IntegerProperty.class},
                {"integer", "int64", LongProperty.class},
                {"number", "float", FloatProperty.class},
                {"number", "double", DoubleProperty.class},
                {"string", null, StringProperty.class},
                {"string", "byte", StringProperty.class}, // are this and the next one correct?
                {"string", "binary", ByteArrayProperty.class},
                {"boolean", null, BooleanProperty.class},
                {"string", "date", DateProperty.class},
                {"string", "date-time", DateTimeProperty.class},
                {"string", "password", StringProperty.class},
            };
    }

    @DataProvider(name = CUSTOM_OR_PLAIN)
    public Object[][] createCustomAndPlainData() {

        // these are the types without formats (as long as not already in the
        // table in the spec), and a "custom" format for each of the types.
        // we expect to get the same Property class back in both cases.
        return new Object[][] {
                {"integer", null, BaseIntegerProperty.class},
                {"integer", "custom", BaseIntegerProperty.class},
                {"number", null, DecimalProperty.class},
                {"number", "custom", DecimalProperty.class},
                {"string", "custom", StringProperty.class},
                {"boolean", "custom", BooleanProperty.class},
                {"object", null, ObjectProperty.class},
                {"object", "custom", ObjectProperty.class},
                {"array", null, ArrayProperty.class},
                {"array", "custom", ArrayProperty.class}
            };
    }

    @Test(dataProvider = BY_IMPLEMENTATION)
    public void testPredefinedProperty(final String type, final String format,
            final Class<? extends Property> expectedClass) {
        buildAndAssertProperty(type, format, expectedClass);
    }

    @Test(dataProvider = FROM_SPEC)
    public void testSpecificationProperty(final String type, final String format,
            final Class<? extends Property> expectedClass) {
        buildAndAssertProperty(type, format, expectedClass);
    }

    @Test(dataProvider = CUSTOM_OR_PLAIN)
    public void testCustomOrPlainProperty(final String type, final String format,
            final Class<? extends Property> expectedClass) {
        buildAndAssertProperty(type, format, expectedClass);
    }

    @Test(dataProvider = STRING_FORMATS)
    public void testStringPredefinedFormats(final String type, final String format,
            final Class<? extends Property> expectedClass) {
        buildAndAssertProperty(type, format, expectedClass);
    }

    private void buildAndAssertProperty(final String type, final String format,
            final Class<? extends Property> expectedClass) {
        Property built = PropertyBuilder.build(type, format, null);
        Assert.assertNotNull(built,
            "Could not build for type: " + type + ", format: " + format + ", expected class: " + expectedClass);
        Assert.assertEquals(built.getClass(), expectedClass);
        Assert.assertEquals(built.getType(), type);
        Assert.assertEquals(built.getFormat(), format);
    }

    @Test
    public void testUnknownType() {
        Assert.assertNull(PropertyBuilder.build("unknownType", "custom", null));
    }

    @Test(dataProvider = FROM_SPEC)
    public void testBuildWithArgs(final String type, final String format,
            final Class<? extends Property> expectedClass) {
        EnumMap<PropertyId, Object> args = new EnumMap<PropertyId, Object>(PropertyId.class);
        args.put(PropertyId.DESCRIPTION, "Example description");

        Property built = PropertyBuilder.build(type, format, args);
        Assert.assertNotNull(built);
        Assert.assertEquals(built.getClass(), expectedClass);
    }
}
