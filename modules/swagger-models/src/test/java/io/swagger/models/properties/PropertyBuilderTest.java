package io.swagger.models.properties;

import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.PropertyBuilder.PropertyId;
import io.swagger.models.properties.StringProperty.Format;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class PropertyBuilderTest {

    private static final String STRING_FORMATS = "stringFormats";
    private static final String FROM_SPEC = "fromSpec";
    private static final String BY_IMPLEMENTATION = "predefined";
    private static final String CUSTOM_OR_PLAIN = "customOrPlain";
    private HashMap<PropertyId, Object> args;
    private List<String> _enum;

    @DataProvider(name = BY_IMPLEMENTATION)
    public Iterator<Object[]> createPredefinedProperties() {
        Property[] properties = {new DecimalProperty(), new FloatProperty(), new DoubleProperty(),
                new BaseIntegerProperty(), new IntegerProperty(), new LongProperty(), new StringProperty(),
                new UUIDProperty(), new BooleanProperty(), new ByteArrayProperty(), new ArrayProperty(),
                new ObjectProperty(), new DateTimeProperty(), new DateProperty(), new RefProperty(),
                new EmailProperty(),
                // new MapProperty() // MapProperty can't be distinguished from
                // ObjectProperty
        };
        List<Object[]> resultList = new ArrayList<Object[]>(properties.length);
        for (Property property : properties) {
            resultList.add(new Object[]{property.getType(), property.getFormat(), property.getClass()});
        }

        return resultList.iterator();
    }

    @DataProvider(name = STRING_FORMATS)
    public Iterator<Object[]> createPredefinedStringFormats() {
        List<Object[]> resultList = new ArrayList<Object[]>();
        for (Format format : StringProperty.Format.values()) {
            resultList.add(new Object[]{StringProperty.TYPE, format.getName(), StringProperty.class});
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
                {"string", "byte", ByteArrayProperty.class},
                {"string", "binary", BinaryProperty.class},
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
        return new Object[][]{{"integer", null, BaseIntegerProperty.class},
                {"integer", "custom", BaseIntegerProperty.class}, {"number", null, DecimalProperty.class},
                {"number", "custom", DecimalProperty.class}, {"string", "custom", StringProperty.class},
                {"boolean", "custom", BooleanProperty.class}, {"object", null, ObjectProperty.class},
                {"object", "custom", ObjectProperty.class}, {"array", null, ArrayProperty.class},
                {"array", "custom", ArrayProperty.class}};
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
        assertNotNull(built,
                "Could not build for type: " + type + ", format: " + format + ", expected class: " + expectedClass);
        assertEquals(built.getClass(), expectedClass);
        assertEquals(built.getType(), type);
        assertEquals(built.getFormat(), format);
    }

    @Test
    public void testUnknownType() {
        assertNull(PropertyBuilder.build("unknownType", "custom", null));
    }

    @Test(dataProvider = FROM_SPEC)
    public void testBuildWithArgs(final String type, final String format,
                                  final Class<? extends Property> expectedClass) {
        EnumMap<PropertyId, Object> args = new EnumMap<PropertyId, Object>(PropertyId.class);
        args.put(PropertyId.DESCRIPTION, "Example description");
        args.put(PropertyId.MIN_LENGTH, 2);
        args.put(PropertyId.MAX_LENGTH, 11);
        args.put(PropertyId.PATTERN, "pattern");
        Property built = PropertyBuilder.build(type, format, args);
        assertNotNull(built);
        assertEquals(built.getClass(), expectedClass);
    }

    @BeforeMethod
    public void setup() {
        args = new HashMap<PropertyBuilder.PropertyId, Object>();
        args.put(PropertyId.READ_ONLY, true);
        String title = "title";
        args.put(PropertyId.TITLE, title);
        String description = "description";
        args.put(PropertyId.DESCRIPTION, description);
        String example = "example";
        args.put(PropertyId.EXAMPLE, example);
        Map<String, Object> vendorExtensions = new HashMap<String, Object>();
        args.put(PropertyId.VENDOR_EXTENSIONS, vendorExtensions);
        _enum = Arrays.asList("4", "hello");
        args.put(PropertyId.ENUM, _enum);
        args.put(PropertyId.DEFAULT, "4");

    }

    @Test
    public void testMergeWithIntegerProperty() {
        // given
        IntegerProperty integerProperty = new IntegerProperty();

        // when
        PropertyBuilder.merge(integerProperty, args);

        // then
        assertTrue(integerProperty.getEnum().contains(4), "Must contain the enum value passed into args");
        assertEquals(integerProperty.getDefault(), (Integer) 4, "Must contain the default value passed into args");

        // given
        args.put(PropertyId.DEFAULT, null);

        // when
        PropertyBuilder.merge(integerProperty, args);

        // then
        assertNull(integerProperty.getDefault(), "Must contain the default value passed into args");
    }

    @Test
    public void testMergeWithBooleanProperty() {
        // given
        args.put(PropertyId.DEFAULT, "true");
        BooleanProperty booleanProperty = new BooleanProperty();

        // when
        PropertyBuilder.merge(booleanProperty, args);

        // then
        assertEquals(booleanProperty.getDefault(), Boolean.TRUE, "Must contain the default value passed into args");

        // given
        args.put(PropertyId.DEFAULT, null);

        // when
        PropertyBuilder.merge(booleanProperty, args);

        // then
        assertNull(booleanProperty.getDefault(), "Must contain the default value passed into args");
    }

    @Test
    public void testMergeWithLongProperty() {
        // given
        args.put(PropertyId.DEFAULT, "4");
        LongProperty longProperty = new LongProperty();

        // when
        PropertyBuilder.merge(longProperty, args);

        // then
        assertTrue(longProperty.getEnum().contains(4L), "Must contain the enum value passed into args");
        assertEquals(longProperty.getDefault(), (Object) 4L, "Must contain the default value passed into args");

        // given
        args.put(PropertyId.DEFAULT, null);

        // when
        PropertyBuilder.merge(longProperty, args);

        // then
        assertNull(longProperty.getDefault(), "Must contain the default value passed into args");
    }

    @Test
    public void testMergeWithFloatProperty() {
        // given
        args.put(PropertyId.DEFAULT, "4");
        FloatProperty floatProperty = new FloatProperty();

        // when
        PropertyBuilder.merge(floatProperty, args);

        // then
        assertTrue(floatProperty.getEnum().contains(4F), "Must contain the enum value passed into args");
        assertEquals(floatProperty.getDefault(), new BigDecimal(4).floatValue(), "Must contain the default value passed into args");

        // given
        args.put(PropertyId.DEFAULT, null);

        // when
        PropertyBuilder.merge(floatProperty, args);

        // then
        assertNull(floatProperty.getDefault(), "Must contain the default value passed into args");
    }

    @Test
    public void testMergeWithUUIDProperty() {
        // given
        UUIDProperty uuidProperty = new UUIDProperty();
        args.put(PropertyId.DEFAULT, "default");
        args.put(PropertyId.MIN_LENGTH, 2);
        args.put(PropertyId.MAX_LENGTH, 11);
        args.put(PropertyId.PATTERN, "pattern");

        // when
        PropertyBuilder.merge(uuidProperty, args);

        // then
        assertEquals(uuidProperty.getDefault(), "default", "Must contain the default value passed into args");
        assertEquals(uuidProperty.getMinLength(), (Object) 2, "Must contain the minLength value passed into args");
        assertEquals(uuidProperty.getMaxLength(), (Object) 11, "Must contain the maxLength value passed into args");
        assertEquals(uuidProperty.getPattern(), "pattern", "Must contain the pattern value passed into args");

        // given
        uuidProperty = Mockito.spy(uuidProperty);
        Mockito.doThrow(new RuntimeException()).when(uuidProperty)._enum(Matchers.anyString());

        // when
        PropertyBuilder.merge(uuidProperty, args);

        // then
        assertEquals(uuidProperty.getEnum(), _enum, "Must contain the enum value passed into args");
    }

    @Test
    public void testMergeWithArrayProperty() {
        // given
        ArrayProperty arrayProperty = new ArrayProperty();
        args.put(PropertyId.MIN_ITEMS, 2);
        args.put(PropertyId.MAX_ITEMS, 11);

        // when
        PropertyBuilder.merge(arrayProperty, args);

        // then
        assertEquals(arrayProperty.getMinItems(), (Object) 2, "Must contain the minItems value passed into args");
        assertEquals(arrayProperty.getMaxItems(), (Object) 11, "Must contain the maxItems value passed into args");
    }

    @Test
    public void testMergeWithDateProperty() {
        // given
        DateProperty dateProperty = new DateProperty();

        // when
        PropertyBuilder.merge(dateProperty, args);

        // then
        assertEquals(dateProperty.getEnum(), _enum, "Must contain the enum value passed into args");

        // given
        dateProperty = Mockito.spy(dateProperty);
        Mockito.doThrow(new RuntimeException()).when(dateProperty)._enum(Matchers.anyString());

        // when
        PropertyBuilder.merge(dateProperty, args);

        // then
        assertEquals(dateProperty.getEnum(), _enum, "Must contain the enum value passed into args");
    }

    @Test
    public void testMergeWithDateTimeProperty() {
        // given
        DateTimeProperty dateTimeProperty = new DateTimeProperty();

        // when
        PropertyBuilder.merge(dateTimeProperty, args);

        // then
        assertEquals(dateTimeProperty.getEnum(), _enum, "Must contain the enum value passed into args");

        // given
        dateTimeProperty = Mockito.spy(dateTimeProperty);
        Mockito.doThrow(new RuntimeException()).when(dateTimeProperty)._enum(Matchers.anyString());

        // when
        PropertyBuilder.merge(dateTimeProperty, args);

        // then
        assertEquals(dateTimeProperty.getEnum(), _enum, "Must contain the enum value passed into args");
    }

    @Test
    public void testMergeWithStringProperty() {
        // given
        StringProperty stringProperty = new StringProperty();

        // when
        PropertyBuilder.merge(stringProperty, args);

        // then
        assertEquals(stringProperty.getEnum(), _enum, "Must contain the enum value passed into args");
    }

    @Test
    public void testMergeWithDoubleProperty() {

        // given
        args.put(PropertyId.MINIMUM, new BigDecimal(2.0));
        args.put(PropertyId.MAXIMUM, new BigDecimal(112.0));
        args.put(PropertyId.EXCLUSIVE_MINIMUM, true);
        args.put(PropertyId.EXCLUSIVE_MAXIMUM, true);
        args.put(PropertyId.MULTIPLE_OF, new BigDecimal(2.0));
        args.put(PropertyId.DEFAULT, "4");
        DoubleProperty doubleProperty = new DoubleProperty();

        // when
        PropertyBuilder.merge(doubleProperty, args);

        // then
        assertTrue(doubleProperty.getEnum().contains(4.0), "Must contain the enum value passed into args");
        assertEquals(doubleProperty.getDefault(), (Double) 4.0, "Must contain the default value passed into args");
        assertEquals(doubleProperty.getMinimum(), new BigDecimal(2.0), "Must contain the minimum value passed into args");
        assertEquals(doubleProperty.getMaximum(), new BigDecimal(112.0), "Must contain the maximum value passed into args");
        assertTrue(doubleProperty.exclusiveMaximum, "Must contain the exclusive minimum value passed into args");
        assertTrue(doubleProperty.exclusiveMinimum, "Must contain the exclusive maximum value passed into args");
        assertEquals(doubleProperty.getMultipleOf(), new BigDecimal(2.0), "Must contain the multiple of value passed into args");

        // given
        args.put(PropertyId.DEFAULT, null);

        // when
        PropertyBuilder.merge(doubleProperty, args);

        // then
        assertNull(doubleProperty.getDefault(), "Must contain the default value passed into args");
    }

    @Test
    public void testToModelWithBooleanProperty() {
        // given
        BooleanProperty booleanProperty = new BooleanProperty();
        booleanProperty.setDescription("description");

        // when
        Model model = PropertyBuilder.toModel(booleanProperty);

        // then
        assertEquals(model.getDescription(), booleanProperty.getDescription(),
                "Must contain the description value passed into the property");
    }

    @Test
    public void testToModelWithIntegerProperty() {
        // given
        IntegerProperty integerProperty = new IntegerProperty();
        integerProperty.setDefault(4);
        Model model = PropertyBuilder.toModel(integerProperty);

        // then
        assertEquals(((ModelImpl) model).getDefaultValue(), 4,
                "Must contain the default value passed into the property");
    }

    @Test
    public void testToModelWithLongProperty() {
        // given
        LongProperty longProperty = new LongProperty();
        longProperty.setDefault(4L);

        // when
        Model model = PropertyBuilder.toModel(longProperty);

        // then
        assertEquals(((ModelImpl) model).getDefaultValue(), 4,
                "Must contain the default value passed into the property");
    }

    @Test
    public void testToModelWithFloatProperty() {
        // given
        FloatProperty floatProperty = new FloatProperty();
        floatProperty.setDefault(4F);

        // when
        Model model = PropertyBuilder.toModel(floatProperty);

        // then
        assertEquals(((ModelImpl) model).getDefaultValue(), new BigDecimal("4.0"),
                "Must contain the default value passed into the property");
    }

    @Test
    public void testToModelWithDoubleProperty() {
        // given
        DoubleProperty doubleProperty = new DoubleProperty();
        doubleProperty.setDefault(4D);

        // when
        Model model = PropertyBuilder.toModel(doubleProperty);

        // then
        assertEquals(((ModelImpl) model).getDefaultValue(), new BigDecimal("4.0"),
                "Must contain the default value passed into the property");
    }

    @Test
    public void testToModelWithRefProperty() {
        // given
        RefProperty refProperty = new RefProperty("ref");

        // when
        refProperty.setDescription("ref description");

        // then
        assertEquals(PropertyBuilder.toModel(refProperty).getDescription(), refProperty.getDescription(),
                "Must contain the description value passed into the property");
    }

    @Test
    public void testToModelWithEmailProperty() {
        // given
        EmailProperty emailProperty = new EmailProperty();
        emailProperty.setDefault("default");

        // when
        Model model = PropertyBuilder.toModel(emailProperty);

        // then
        assertEquals(((ModelImpl) model).getDefaultValue(), "default",
                "Must contain the default value passed into the property");
    }

    @Test
    public void testToModelWithArrayProperty() {
        // given
        EmailProperty emailProperty = new EmailProperty();
        ArrayProperty arrayProperty = new ArrayProperty();
        arrayProperty.setItems(emailProperty);

        // when
        Model model = PropertyBuilder.toModel(arrayProperty);

        // then
        assertEquals(((ArrayModel) model).getItems(), emailProperty,
                "Must contain the items value passed into the property");
    }

    @Test
    public void testToModelWithMapProperty() {
        // given
        EmailProperty emailProperty = new EmailProperty();
        MapProperty mapProperty = new MapProperty();
        mapProperty.setAdditionalProperties(emailProperty);

        // when
        Model model = PropertyBuilder.toModel(mapProperty);

        // then
        assertEquals(((ModelImpl) model).getAdditionalProperties(), emailProperty,
                "Must contain the additionalProperties value passed into the property");
    }

    @Test
    public void testToModelWithStringProperty() {
        // given
        StringProperty stingProperty = new StringProperty();
        stingProperty.setDefault("default");

        // when
        Model model = PropertyBuilder.toModel(stingProperty);

        // then
        assertEquals(((ModelImpl) model).getDefaultValue(), "default",
                "Must contain the default value passed into the property");
    }

    @Test
    public void testToModelWithUUIDProperty() {
        // given
        UUIDProperty uuidProperty = new UUIDProperty();
        uuidProperty.setDefault("default");

        // when
        Model model = PropertyBuilder.toModel(uuidProperty);

        // then
        assertEquals(((ModelImpl) model).getDefaultValue(), "default",
                "Must contain the default value passed into the property");
    }

    @Test
    public void testToModelWithUnknownPropertyType() {
        assertNull(PropertyBuilder.toModel(Mockito.mock(Property.class)));
    }

    @Test
    public void testGetPropertyName() {
        // when
        String name = PropertyId.DEFAULT.getPropertyName();

        // then
        assertEquals(name, "default", "Property name for DEFAULT is 'default'");
    }

    @Test
    public void testValueOf() {
        // when
        PropertyId value = PropertyId.valueOf("DEFAULT");

        // then
        assertEquals(value, PropertyId.DEFAULT, "Value of 'DEFAULT' is DEFAULT");
    }
}
