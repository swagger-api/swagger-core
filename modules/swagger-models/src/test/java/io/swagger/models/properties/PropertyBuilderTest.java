package io.swagger.models.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
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
    
    @Test
    public void testMerge(){    	
    	Map<PropertyId, Object> args=new HashMap<PropertyBuilder.PropertyId, Object>();
    	args.put(PropertyId.READ_ONLY, true);
    	String title="title";
    	args.put(PropertyId.TITLE, title);
    	String description="description";
    	args.put(PropertyId.DESCRIPTION, description);
    	String example="example";
    	args.put(PropertyId.EXAMPLE, example);
    	Map<String, Object> vendorExtensions=new HashMap<String, Object>();
    	args.put(PropertyId.VENDOR_EXTENSIONS, vendorExtensions);
    	
    	List<String>_enum=Arrays.asList("4","hello");
    	args.put(PropertyId.ENUM, _enum);
    	args.put(PropertyId.DEFAULT, "4");
    	IntegerProperty  integerProperty=new IntegerProperty();
    	PropertyBuilder.merge(integerProperty, args);
    	Assert.assertTrue(integerProperty.getEnum().contains(4));
    	Assert.assertEquals(integerProperty.getDefault(),(Integer) 4);
    	args.put(PropertyId.DEFAULT, null);
    	PropertyBuilder.merge(integerProperty, args);
    	Assert.assertNull(integerProperty.getDefault());
    	
    	args.put(PropertyId.DEFAULT, "true");
    	BooleanProperty  booleanProperty=new BooleanProperty();
    	PropertyBuilder.merge(booleanProperty, args);
    	Assert.assertEquals(booleanProperty.getDefault(),Boolean.TRUE);
    	args.put(PropertyId.DEFAULT, null);
    	PropertyBuilder.merge(booleanProperty, args);
    	Assert.assertNull(booleanProperty.getDefault());
    	
    	args.put(PropertyId.DEFAULT, "4");
    	LongProperty  longProperty=new LongProperty();
    	PropertyBuilder.merge(longProperty, args);
    	Assert.assertTrue(longProperty.getEnum().contains(4L));
    	Assert.assertEquals(longProperty.getDefault(),(Object)4L);
    	args.put(PropertyId.DEFAULT, null);
    	PropertyBuilder.merge(longProperty, args);
    	Assert.assertNull(longProperty.getDefault());
    	
    	args.put(PropertyId.DEFAULT, "4");
    	FloatProperty  floatProperty=new FloatProperty();
    	PropertyBuilder.merge(floatProperty, args);
    	Assert.assertTrue(floatProperty.getEnum().contains(4F));
    	Assert.assertEquals(floatProperty.getDefault(),(Float) 4F);
    	args.put(PropertyId.DEFAULT, null);
    	PropertyBuilder.merge(floatProperty, args);
    	Assert.assertNull(floatProperty.getDefault());
    	
    	
    	
    	UUIDProperty uuidProperty=new UUIDProperty();
    	args.put(PropertyId.DEFAULT, "default");
    	args.put(PropertyId.MIN_LENGTH, 2);
    	args.put(PropertyId.MAX_LENGTH, 11);
    	args.put(PropertyId.PATTERN, "pattern");
    	PropertyBuilder.merge(uuidProperty, args);
    	Assert.assertEquals(uuidProperty.getDefault(), "default");
    	Assert.assertEquals(uuidProperty.getMinLength(), (Object)2);
    	Assert.assertEquals(uuidProperty.getMaxLength(), (Object)11);
    	Assert.assertEquals(uuidProperty.getPattern(), "pattern");
    	
    	uuidProperty=Mockito.spy(uuidProperty);
    	Mockito.doThrow(new RuntimeException()).when(uuidProperty)._enum(Matchers.anyString()); 
    	PropertyBuilder.merge(uuidProperty, args);
    	Assert.assertEquals(uuidProperty.getEnum(), _enum);
    	
    	ArrayProperty arrayProperty=new ArrayProperty();
    	args.put(PropertyId.MIN_ITEMS, 2);
    	args.put(PropertyId.MAX_ITEMS, 11);
    	PropertyBuilder.merge(arrayProperty, args);
    	Assert.assertEquals(arrayProperty.getMinItems(), (Object)2);
    	Assert.assertEquals(arrayProperty.getMaxItems(), (Object)11);
    	
    	DateProperty dateProperty=new DateProperty();
    	PropertyBuilder.merge(dateProperty, args);
    	Assert.assertEquals(dateProperty.getEnum(), _enum);
    	
    	dateProperty=Mockito.spy(dateProperty);
    	Mockito.doThrow(new RuntimeException()).when(dateProperty)._enum(Matchers.anyString()); 
    	PropertyBuilder.merge(dateProperty, args);
    	Assert.assertEquals(dateProperty.getEnum(), _enum);
    	
    	
    	DateTimeProperty dateTimeProperty=new DateTimeProperty();
    	PropertyBuilder.merge(dateTimeProperty, args);
    	Assert.assertEquals(dateTimeProperty.getEnum(), _enum);
    	
    	dateTimeProperty=Mockito.spy(dateTimeProperty);
    	Mockito.doThrow(new RuntimeException()).when(dateTimeProperty)._enum(Matchers.anyString()); 
    	PropertyBuilder.merge(dateTimeProperty, args);
    	Assert.assertEquals(dateTimeProperty.getEnum(), _enum);
    	
    	StringProperty stringProperty=new StringProperty();
    	PropertyBuilder.merge(stringProperty, args);
    	Assert.assertEquals(stringProperty.getEnum(), _enum);
    	
    	args.put(PropertyId.MINIMUM, 2.0);
    	args.put(PropertyId.MAXIMUM, 112.0);
    	args.put(PropertyId.EXCLUSIVE_MINIMUM, true);
    	args.put(PropertyId.EXCLUSIVE_MAXIMUM, true);
    	args.put(PropertyId.DEFAULT, "4");
    	DoubleProperty  doubleProperty=new DoubleProperty();
    	PropertyBuilder.merge(doubleProperty, args);
    	Assert.assertTrue(doubleProperty.getEnum().contains(4.0));
    	Assert.assertEquals(doubleProperty.getDefault(),(Double) 4.0);
    	Assert.assertEquals(doubleProperty.getMinimum(), 2.0);
    	Assert.assertEquals(doubleProperty.getMaximum(), 112.0);
    	Assert.assertTrue(doubleProperty.exclusiveMaximum);
    	Assert.assertTrue(doubleProperty.exclusiveMinimum);
    	args.put(PropertyId.DEFAULT, null);
    	PropertyBuilder.merge(doubleProperty, args);
    	Assert.assertNull(doubleProperty.getDefault());
    	
    }
    
    @Test
    public void testToModel(){
    	Assert.assertNull(PropertyBuilder.toModel(Mockito.mock(Property.class)));
    	
    	BooleanProperty booleanProperty=new BooleanProperty();
    	booleanProperty.setDescription("description");
    	Model model=PropertyBuilder.toModel(booleanProperty);
    	Assert.assertEquals(model.getDescription(), booleanProperty.getDescription());
    	
    	IntegerProperty integerProperty=new IntegerProperty();
    	integerProperty.setDefault(4);
    	model=PropertyBuilder.toModel(integerProperty);
    	Assert.assertEquals(((ModelImpl)model).getDefaultValue(), "4");
    	
    	LongProperty longProperty=new LongProperty();
    	longProperty.setDefault(4L);
    	model=PropertyBuilder.toModel(longProperty);
    	Assert.assertEquals(((ModelImpl)model).getDefaultValue(), "4");
    	
    	FloatProperty floatProperty=new FloatProperty();
    	floatProperty.setDefault(4F);
    	model=PropertyBuilder.toModel(floatProperty);
    	Assert.assertEquals(((ModelImpl)model).getDefaultValue(), "4.0");
    	
    	DoubleProperty doubleProperty=new DoubleProperty();
    	doubleProperty.setDefault(4D);
    	model=PropertyBuilder.toModel(doubleProperty);
    	Assert.assertEquals(((ModelImpl)model).getDefaultValue(), "4.0");
    	
    	RefProperty refProperty=new RefProperty("ref");
    	refProperty.setDescription("ref description");
    	Assert.assertEquals(PropertyBuilder.toModel(refProperty).getDescription(), refProperty.getDescription());
    	
    	EmailProperty emailProperty=new EmailProperty();
    	emailProperty.setDefault("default");
    	model=PropertyBuilder.toModel(emailProperty);
    	Assert.assertEquals(((ModelImpl)model).getDefaultValue(), "default");
    	
    	ArrayProperty arrayProperty=new ArrayProperty();
    	arrayProperty.setItems(emailProperty);
    	model=PropertyBuilder.toModel(arrayProperty);
    	Assert.assertEquals(((ArrayModel)model).getItems(), emailProperty);
    	
    	MapProperty mapProperty=new MapProperty();
    	mapProperty.setAdditionalProperties(emailProperty);
    	model=PropertyBuilder.toModel(mapProperty);
    	Assert.assertEquals(((ModelImpl)model).getAdditionalProperties(), emailProperty);
    	
    	StringProperty stingProperty=new StringProperty();
    	stingProperty.setDefault("default");
    	model=PropertyBuilder.toModel(stingProperty);
    	Assert.assertEquals(((ModelImpl)model).getDefaultValue(), "default");
    	
    	UUIDProperty uuidProperty=new UUIDProperty();
    	uuidProperty.setDefault("default");
    	model=PropertyBuilder.toModel(uuidProperty);
    	Assert.assertEquals(((ModelImpl)model).getDefaultValue(), "default");
    	
    	
    	
    }
    
    @Test
    public void testGetPropertyName(){
    	Assert.assertEquals(PropertyId.DEFAULT.getPropertyName(), "default");
    	Assert.assertEquals(PropertyId.valueOf("DEFAULT"), PropertyId.DEFAULT);
    }
}
