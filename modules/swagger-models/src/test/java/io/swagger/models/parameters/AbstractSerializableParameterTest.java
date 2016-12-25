package io.swagger.models.parameters;

import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BaseIntegerProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.DecimalProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class AbstractSerializableParameterTest {

    private AbstractSerializableParameter<?> instance;

    private String example;

    private Object defaultValue;

    @BeforeMethod
    public void setup() {
        instance = new CookieParameter();
        defaultValue = null;
    }

    /*
     * Tests getters and setters methods on {@link
     * AbstractSerializableParameter} It was not possible to cove it with {@link
     * io.swagger.PojosTest} so a manual implementation is provided for now TODO
     * improve PojosTest to test getters and setters for abstracts classes
     */
    @Test
    public void testGettersAndSetters() {
        // given
        String type = "type";

        // when
        instance.setType(type);

        // then
        assertEquals(instance.getType(), type, "The get type must be the same as the set one");

        // given
        String format = "format";

        // when
        instance.setFormat(format);

        // then
        assertEquals(instance.getFormat(), format, "The get format must be the same as the set one");

        // given
        String collectionFormat = "collectionFormat";

        // when
        instance.setCollectionFormat(collectionFormat);

        // then
        assertEquals(instance.getCollectionFormat(), collectionFormat,
                "The get collectionFormat must be the same as the set one");

        // given
        Property items = new BooleanProperty();

        // when
        instance.setItems(items);

        // then
        assertEquals(instance.getItems(), items, "The get items must be the same as the set one");

        // given
        List<String> _enum = Arrays.asList("_enum");

        // when
        instance._enum(_enum);
        instance.setEnum(_enum);

        // then
        assertEquals(instance.getEnum(), _enum, "The get _enum must be the same as the set one");

        // given
        Boolean exclusiveMaximum = true;

        // when
        instance.setExclusiveMaximum(exclusiveMaximum);

        // then
        assertEquals(instance.isExclusiveMaximum(), exclusiveMaximum,
                "The get exclusiveMaximum must be the same as the set one");

        // given
        Double maximum = 1.0;

        // when
        instance.setMaximum(new BigDecimal(maximum));

        // then
        assertEquals(instance.getMaximum(), new BigDecimal(maximum), "The get maximum must be the same as the set one");

        // given
        Boolean exclusiveMinimum = true;

        // when
        instance.setExclusiveMinimum(exclusiveMinimum);

        // then
        assertEquals(instance.isExclusiveMinimum(), exclusiveMinimum,
                "The get exclusiveMinimum must be the same as the set one");

        // given
        Double minimum = 0.1;

        // when
        instance.setMinimum(new BigDecimal(minimum));

        // then
        assertEquals(instance.getMinimum(), new BigDecimal(minimum), "The get minimum must be the same as the set one");

        // given
        String example = "example";

        // when
        instance.setExample(example);

        // then
        assertEquals(instance.getExample(), example, "The get example must be the same as the set one");

        // given
        Integer maxItems = 100;

        // when
        instance.setMaxItems(maxItems);

        // then
        assertEquals(instance.getMaxItems(), maxItems, "The get maxItems must be the same as the set one");

        // given
        Integer minItems = 10;

        // when
        instance.setMinItems(minItems);

        // then
        assertEquals(instance.getMinItems(), minItems, "The get minItems must be the same as the set one");

        // given
        Integer maxLength = 500;

        // when
        instance.setMaxLength(maxLength);

        // then
        assertEquals(instance.getMaxLength(), maxLength, "The get maxLength must be the same as the set one");

        // given
        Integer minLength = 25;

        // when
        instance.setMinLength(minLength);

        // then
        assertEquals(instance.getMinLength(), minLength, "The get minLength must be the same as the set one");

        // given
        String pattern = "String pattern";

        // when
        instance.setPattern(pattern);

        // then
        assertEquals(instance.getPattern(), pattern, "The get pattern must be the same as the set one");

        // given
        Boolean uniqueItems = true;

        // when
        instance.setUniqueItems(uniqueItems);

        // then
        assertEquals(instance.isUniqueItems(), uniqueItems, "The get uniqueItems must be the same as the set one");

        // given
        Number multipleOf = 5;

        // when
        instance.setMultipleOf(multipleOf);

        // then
        assertEquals(instance.getMultipleOf(), multipleOf, "The get multipleOf must be the same as the set one");

        // given
        String defaultValue = "defaultValue";

        // when
        instance.setDefaultValue(defaultValue);

        // then
        assertEquals(instance.getDefaultValue(), defaultValue, "The get defaultValue must be the same as the set one");

        // when
        instance.required(true);

        // then
        assertTrue(instance.getRequired(), "The get required must be the same as the set one");

        // given
        StringProperty property = new StringProperty();
        property._enum(_enum);

        // when
        instance.property(property);

        // then
        assertEquals(instance.getEnum(), _enum, "The get _enum must be the same as the set one");
        assertEquals(instance.getType(), property.getType(), "The get type must be the same as the set property type");

        // given
        ArrayProperty arrayProperty = new ArrayProperty();

        // when
        arrayProperty.items(items);
        instance.property(arrayProperty);

        // then
        assertEquals(instance.getItems(), items, "The get items must be the same as the set one");
        assertEquals(instance.getType(), arrayProperty.getType(),
                "The get type must be the same as the set property type");
        assertEquals(instance.getDefaultCollectionFormat(), "csv", "The get collection format must be csv");
    }

    @Test
    public void testGetDefaultWithBaseIntegerProperty() {
        // given
        instance.setProperty(new BaseIntegerProperty());
        defaultValue = 14;

        // when
        instance.setDefault(defaultValue);

        // then
        assertEquals(instance.getDefault(), 14L, "The get default must be the same as the set one");
    }

    @Test
    public void testGetDefaultWithDecimalProperty() {
        // given
        instance.setProperty(new DecimalProperty());
        defaultValue = 14.1;

        // when
        instance.setDefault(defaultValue);

        // then
        assertEquals(instance.getDefault(), 14.1, "The get default must be the same as the set one");

        // given
        defaultValue = "wrong format";

        // when
        instance.setDefault(defaultValue);

        // then
        assertEquals(instance.getDefault(), defaultValue, "The get default must be the same as the set one");

        // when
        instance.setProperty(new ArrayProperty());
        assertEquals(instance.getDefault(), defaultValue, "Default must not change when we set an array property");
    }

    @Test
    public void testGetDefaultWithBooleanProperty() {
        // given
        instance.setProperty(new BooleanProperty());
        defaultValue = true;

        // when
        instance.setDefault(defaultValue);

        // then
        assertEquals(instance.getDefault(), true, "The get default must be the same as the set one");
    }

    @Test
    public void testGetDefault() {
        assertNull(instance.getDefault(), "The default value must be null for any new instance");
    }

    @Test
    public void testGetExampleWithBaseIntegerProperty() {
        // given
        instance.setProperty(new BaseIntegerProperty());
        String example = "14";

        // when
        instance.setExample(example);

        // then
        assertEquals(instance.getExample(), 14L, "The get example must be the same as the set one");
    }

    @Test
    public void testGetExampleWithDecimalProperty() {
        // given
        instance.setProperty(new DecimalProperty());
        example = "14.1";

        // when
        instance.setExample(example);

        // then
        assertEquals(instance.getExample(), 14.1, "The get example must be the same as the set one");

        // given
        example = "wrong format";

        // when
        instance.setExample(example);

        // then
        assertEquals(instance.getExample(), example, "The example value must not change when the format is wrong");

        // when
        instance.setProperty(new ArrayProperty());

        // then
        assertEquals(instance.getExample(), example,
                "The example value must not change when when set an array property");
    }

    @Test
    public void testGetExampleWithBooleanProperty() {
        // given
        instance.setProperty(new BooleanProperty());
        example = "true";

        // when
        instance.setExample(example);
        assertEquals(instance.getExample(), true, "The get example must be the same as the set one");
    }

    @Test
    public void testGetExample() {
        assertNull(instance.getExample(), "A new instance example must be null");
    }
}
