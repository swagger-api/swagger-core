package io.swagger.v3.core.serialization;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class Oas31ObjectFieldTest {
    @Test(description = "Repro #4682: In OAS 3.1, raw Object property should not be rendered as an empty schema")
    public void rawObjectPropertyShouldBeObjectSchema() {
        Map<String, Schema> schemas = ModelConverters.getInstance(true).readAll(PojoUsingObjectField.class);
        Schema pojo = schemas.get("PojoUsingObjectField");
        Assert.assertNotNull(pojo, "PojoUsingObjectField schema should exist");

        Schema myField = (Schema) pojo.getProperties().get("myField");
        Assert.assertNotNull(myField, "myField schema should exist");
        Assert.assertTrue(isObjectSchema(myField), "Expected raw Object property to be an object schema in OAS 3.1, but was: type=" + myField.getType() + ", types=" + myField.getTypes() + ", class=" + myField.getClass());
    }

    @Test(description = "OAS 3.1: List<Object> items schema should be object")
    public void listOfObjectItemsShouldBeObjectSchema() {
        Map<String, Schema> schemas = ModelConverters.getInstance(true).readAll(PojoUsingListOfObject.class);
        Schema pojo = schemas.get("PojoUsingListOfObject");
        Assert.assertNotNull(pojo, "PojoUsingListOfObject schema should exist");
        Schema itemsProp = (Schema) pojo.getProperties().get("items");
        Assert.assertNotNull(itemsProp, "items property schema should exist");
        Schema itemSchema = itemsProp.getItems();
        Assert.assertNotNull(itemSchema, "List<Object> items schema should exist");
        Assert.assertTrue(isObjectSchema(itemSchema), "Expected List<Object> items to be an object schema in OAS 3.1, but was: type=" + itemSchema.getType() + ", types=" + itemSchema.getTypes() + ", class=" + itemSchema.getClass());
    }

    @Test(description = "OAS 3.1: Map<String,Object> additionalProperties schema should be object")
    public void mapOfObjectAdditionalPropertiesShouldBeObjectSchema() {
        Map<String, Schema> schemas = ModelConverters.getInstance(true).readAll(PojoUsingMapStringToObject.class);
        Schema pojo = schemas.get("PojoUsingMapStringToObject");
        Assert.assertNotNull(pojo, "PojoUsingMapStringToObject schema should exist");
        Schema additionalProp = (Schema) pojo.getProperties().get("additional");
        Assert.assertNotNull(additionalProp, "additional property schema should exist");
        Schema valueSchema = (Schema) additionalProp.getAdditionalProperties();
        Assert.assertNotNull(valueSchema, "Map<String,Object> additionalProperties (value schema) should exist");
        Assert.assertTrue(isObjectSchema(valueSchema), "Expected Map<String,Object> additionalProperties to be an object schema in OAS 3.1, but was: type=" + valueSchema.getType() + ", types=" + valueSchema.getTypes() + ", class=" + valueSchema.getClass());
    }

    /**
     * OAS 3.1 may represent types via Schema#getTypes() (JSON Schema style) rather than Schema#getType().
     */
    private static boolean isObjectSchema(Schema s) {
        if (s == null) return false;
        if ("object".equals(s.getType())) return true;
        return s.getTypes() != null && s.getTypes().contains("object");
    }

    private static class PojoUsingObjectField {
        private Object myField;

        public Object getMyField() {
            return myField;
        }

        public void setMyField(Object myField) {
            this.myField = myField;
        }
    }

    private static class PojoUsingListOfObject {
        private List<Object> items;

        public List<Object> getItems() {
            return items;
        }

        public void setItems(List<Object> items) {
            this.items = items;
        }
    }

    private static class PojoUsingMapStringToObject {
        private Map<String, Object> additional;

        public Map<String, Object> getAdditional() {
            return additional;
        }

        public void setAdditional(Map<String, Object> additional) {
            this.additional = additional;
        }
    }
}