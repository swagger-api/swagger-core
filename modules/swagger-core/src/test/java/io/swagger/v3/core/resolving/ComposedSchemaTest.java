package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.resolving.resources.TestObject2616;
import io.swagger.v3.core.resolving.resources.TestObjectTicket2620;
import io.swagger.v3.core.resolving.resources.TestObjectTicket2620Subtypes;
import io.swagger.v3.core.resolving.resources.TestObjectTicket2900;
import io.swagger.v3.core.resolving.resources.TestObjectTicket4247;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.MapperBuilder;

import java.util.Map;
import java.util.function.Consumer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class ComposedSchemaTest {

    @Test(description = "read composed schem refs #2620")
    public void readBilateralComposedSchema_ticket2620() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestObjectTicket2620.class);
        Schema model = schemas.get("Child2TestObject");
        Map<String, Schema> properties = model.getProperties();
        assertNotNull(properties.get("name"));
        assertNotNull(properties.get("childName"));
        assertFalse(model instanceof ComposedSchema);
        model = schemas.get("ChildTestObject");
        properties = model.getProperties();
        assertNotNull(properties.get("name"));
        assertNotNull(properties.get("childName"));
        assertFalse(model instanceof ComposedSchema);

        model = schemas.get("TestObjectTicket2620");
        properties = model.getProperties();
        assertNotNull(properties.get("name"));
        assertNull(properties.get("childName"));
        assertTrue(model instanceof ComposedSchema);
        assertEquals(((ComposedSchema) model).getOneOf().size(), 2);

        schemas = ModelConverters.getInstance().readAll(TestObjectTicket2620Subtypes.class);
        model = schemas.get("Child2TestObject");
        assertNull(model.getProperties());
        properties = ((ComposedSchema)model).getAllOf().get(1).getProperties();
        assertNull(properties.get("name"));
        assertNotNull(properties.get("childName"));
        assertTrue(model instanceof ComposedSchema);
        model = schemas.get("ChildTestObject");
        assertNull(model.getProperties());
        properties = ((ComposedSchema)model).getAllOf().get(1).getProperties();
        assertNull(properties.get("name"));
        assertNotNull(properties.get("childName"));
        assertTrue(model instanceof ComposedSchema);
        assertEquals(((ComposedSchema) model).getAllOf().size(), 2);

        model = schemas.get("TestObjectTicket2620Subtypes");
        properties = model.getProperties();
        assertNotNull(properties.get("name"));
        assertNull(properties.get("childName"));
        assertTrue(model instanceof ComposedSchema);
        assertEquals(((ComposedSchema) model).getOneOf().size(), 2);
    }

    @Test(description = "read composed schem refs #2900")
    public void readComposedSchema_ticket2900() {
        ModelConverters.reset();

        Consumer<MapperBuilder<? extends ObjectMapper, ? extends MapperBuilder<?, ?>>> mapperBuilderCustomizer
                = mapperBuilder -> mapperBuilder
                .addMixIn(TestObjectTicket2900.GsonJsonPrimitive.class, TestObjectTicket2900.GsonJsonPrimitiveMixIn.class);
        Map<String, Schema> schemas = ModelConverters.getInstance(mapperBuilderCustomizer).readAll(TestObjectTicket2900.class);
        Schema model = schemas.get("SomeDTO");
        assertNotNull(model);
        Map<String, Schema> properties = model.getProperties();
        assertNotNull(properties.get("value"));
        assertEquals(properties.get("value").get$ref(), "#/components/schemas/MyJsonPrimitive");
        assertEquals(properties.get("valueWithMixIn").get$ref(), "#/components/schemas/GsonJsonPrimitive");

        model = schemas.get("MyJsonPrimitive");
        assertNotNull(model);
        assertEquals(((ComposedSchema) model).getOneOf().size(), 2);
        assertEquals(((ComposedSchema)model).getOneOf().get(0).getType(), "string");
        assertEquals(((ComposedSchema)model).getOneOf().get(1).getType(), "number");

        model = schemas.get("GsonJsonPrimitive");
        assertNotNull(model);
        assertEquals(((ComposedSchema) model).getOneOf().size(), 2);
        assertEquals(((ComposedSchema)model).getOneOf().get(0).getType(), "string");
        assertEquals(((ComposedSchema)model).getOneOf().get(1).getType(), "number");
        assertNull(model.getProperties());

        ModelConverters.reset();
    }

    @Test(description = "read composed schem refs #2616")
    public void readArrayComposedSchema_ticket2616() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestObject2616.class);
        Schema model = schemas.get("testObject");
        assertNotNull(model);
        Map<String, Schema> properties = model.getProperties();
        assertNotNull(properties.get("objects"));
        assertTrue(properties.get("objects") instanceof ArraySchema);
        model = schemas.get("AbstractObject");
        assertNotNull(model);
        assertTrue(model instanceof ComposedSchema);
        assertEquals(((ComposedSchema) model).getOneOf().size(), 2);
        model = schemas.get("AObject");
        assertNotNull(model);
        model = schemas.get("BObject");
        assertNotNull(model);
        model = schemas.get("objects");
        assertNull(model);

    }

    @Test(description = "read single composed schem refs #2616")
    public void readComposedSchema_ticket2616() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestObject2616.TestObject2616_Schema.class);
        Schema model = schemas.get("TestObject2616_Schema");
        assertNotNull(model);
        Map<String, Schema> properties = model.getProperties();
        assertNotNull(properties.get("object"));
        assertEquals(properties.get("object").get$ref(), "#/components/schemas/AbstractObject");
        model = schemas.get("AbstractObject");
        assertNotNull(model);
        assertTrue(model instanceof ComposedSchema);
        assertEquals(((ComposedSchema) model).getOneOf().size(), 2);
        model = schemas.get("AObject");
        assertNotNull(model);
        model = schemas.get("BObject");
        assertNotNull(model);
        model = schemas.get("objects");
        assertNull(model);
    }

    @Test(description = "read composed schem refs #4247")
    public void readComposedSchema_ticket4247() {
        Map<String, Schema> schemas = ModelConverters.getInstance().readAll(TestObjectTicket4247.class);
        Schema model = schemas.get("TestObjectTicket4247");
        assertNotNull(model);
        Map<String, Schema> properties = model.getProperties();
        assertNotNull(properties.get("value"));
        assertEquals(((ComposedSchema) properties.get("value")).getOneOf().size(), 2);
        assertEquals(((ComposedSchema)properties.get("value")).getOneOf().get(0).getType(), "string");
        assertEquals(((ComposedSchema)properties.get("value")).getOneOf().get(1).getType(), "number");
    }
}
