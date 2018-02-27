package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.resolving.resources.TestObject2616;
import io.swagger.v3.core.resolving.resources.TestObjectTicket2620;
import io.swagger.v3.core.resolving.resources.TestObjectTicket2620Subtypes;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class ComposedSchemaTest {

    @Test(description = "read composed schem refs #2620")
    public void readBilateralComposedSchema_ticket2620() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestObjectTicket2620.class);
        Schema model = schemas.get("Child2TestObject");
        Map<String, Schema> properties = model.getProperties();
        Assert.assertNotNull(properties.get("name"));
        Assert.assertNotNull(properties.get("childName"));
        Assert.assertFalse(model instanceof ComposedSchema);
        model = schemas.get("ChildTestObject");
        properties = model.getProperties();
        Assert.assertNotNull(properties.get("name"));
        Assert.assertNotNull(properties.get("childName"));
        Assert.assertFalse(model instanceof ComposedSchema);

        model = schemas.get("TestObjectTicket2620");
        properties = model.getProperties();
        Assert.assertNotNull(properties.get("name"));
        Assert.assertNull(properties.get("childName"));
        Assert.assertTrue(model instanceof ComposedSchema);
        Assert.assertTrue(((ComposedSchema)model).getOneOf().size() == 2);

        schemas = ModelConverters.getInstance().readAll(TestObjectTicket2620Subtypes.class);
        model = schemas.get("Child2TestObject");
        properties = model.getProperties();
        Assert.assertNull(properties.get("name"));
        Assert.assertNotNull(properties.get("childName"));
        Assert.assertTrue(model instanceof ComposedSchema);
        model = schemas.get("ChildTestObject");
        properties = model.getProperties();
        Assert.assertNull(properties.get("name"));
        Assert.assertNotNull(properties.get("childName"));
        Assert.assertTrue(model instanceof ComposedSchema);
        Assert.assertTrue(((ComposedSchema)model).getAllOf().size() == 1);

        model = schemas.get("TestObjectTicket2620Subtypes");
        properties = model.getProperties();
        Assert.assertNotNull(properties.get("name"));
        Assert.assertNull(properties.get("childName"));
        Assert.assertTrue(model instanceof ComposedSchema);
        Assert.assertTrue(((ComposedSchema)model).getOneOf().size() == 2);
    }

    @Test(description = "read composed schem refs #2616")
    public void readArrayComposedSchema_ticket2616() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestObject2616.class);
        Schema model = schemas.get("testObject");
        Assert.assertNotNull(model);
        Map<String, Schema> properties = model.getProperties();
        Assert.assertNotNull(properties.get("objects"));
        Assert.assertTrue(properties.get("objects") instanceof ArraySchema);
        model = schemas.get("AbstractObject");
        Assert.assertNotNull(model);
        Assert.assertTrue(model instanceof ComposedSchema);
        Assert.assertTrue(((ComposedSchema)model).getOneOf().size() == 2);
        model = schemas.get("AObject");
        Assert.assertNotNull(model);
        model = schemas.get("BObject");
        Assert.assertNotNull(model);
        model = schemas.get("objects");
        Assert.assertNull(model);

    }

    @Test(description = "read single composed schem refs #2616")
    public void readComposedSchema_ticket2616() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestObject2616.TestObject2616_Schema.class);
        Schema model = schemas.get("TestObject2616_Schema");
        Assert.assertNotNull(model);
        Map<String, Schema> properties = model.getProperties();
        Assert.assertNotNull(properties.get("object"));
        Assert.assertTrue(properties.get("object").get$ref().equals("#/components/schemas/AbstractObject"));
        model = schemas.get("AbstractObject");
        Assert.assertNotNull(model);
        Assert.assertTrue(model instanceof ComposedSchema);
        Assert.assertTrue(((ComposedSchema)model).getOneOf().size() == 2);
        model = schemas.get("AObject");
        Assert.assertNotNull(model);
        model = schemas.get("BObject");
        Assert.assertNotNull(model);
        model = schemas.get("objects");
        Assert.assertNull(model);
    }

}
