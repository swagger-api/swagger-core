package io.swagger.util;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BinaryProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.ByteArrayProperty;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.DecimalProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.EmailProperty;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.FloatProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.PasswordProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.models.properties.UUIDProperty;

public class PrimitiveTypeTest {

    @DataProvider(name = "PrimitiveTypes")
    public Object[][] createPrimitiveTypes() {
        final PrimitiveType[] values = PrimitiveType.values();
        final Object[][] data = new Object[values.length][1];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = values[i];
        }
        return data;
    }

    @DataProvider(name = "PropertyTypes")
    public Object[][] createPropertyTypes() {
        // @formatter:off
        return new Object[][] { 
            { new IntegerProperty(), Boolean.TRUE },
            { new LongProperty(), Boolean.TRUE },
            { new DecimalProperty(), Boolean.TRUE },
            { new DoubleProperty(), Boolean.TRUE },
            { new FloatProperty(), Boolean.TRUE },
            { new ArrayProperty(), Boolean.FALSE},
            { new BinaryProperty(), Boolean.TRUE },
            { new BooleanProperty(), Boolean.TRUE },
            { new DateProperty(), Boolean.TRUE },
            { new DateTimeProperty(), Boolean.TRUE },
            { new FileProperty(), Boolean.TRUE },
            { new MapProperty(), Boolean.FALSE},
            { new ObjectProperty(), Boolean.FALSE},
            { new PasswordProperty(), Boolean.TRUE },
            { new RefProperty(), Boolean.FALSE},
            { new StringProperty(), Boolean.TRUE },
            { new ByteArrayProperty(), Boolean.TRUE },
            { new EmailProperty(), Boolean.TRUE },
            { new UUIDProperty(), Boolean.TRUE }
        };
        // @formatter:on
    }

    // Easier to have this individual test for debugging
    @Test()
    public void testMapProperty() {
        assertEquals(PrimitiveType.isSwaggerPrimitive(new MapProperty()), false);
    }

    // Easier to have this individual test for debugging
    @Test()
    public void testObjectProperty() {
        assertEquals(PrimitiveType.isSwaggerPrimitive(new ObjectProperty()), false);
    }

    @Test(dataProvider = "PrimitiveTypes")
    public void testPrimitiveType(final PrimitiveType primitiveType) {
        final boolean expectedPrimitive = primitiveType != PrimitiveType.OBJECT;
        assertEquals(PrimitiveType.isSwaggerPrimitive(primitiveType.createProperty()), expectedPrimitive);
    }

    @Test(dataProvider = "PropertyTypes")
    public void testPropertyType(final Property property, final boolean expectedPrimitive) {
        assertEquals(PrimitiveType.isSwaggerPrimitive(property), expectedPrimitive);
    }

}
