package io.swagger;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import io.swagger.jaxrs.Reader;
import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.RefModel;
import io.swagger.models.Swagger;
import io.swagger.models.TestEnum;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.models.properties.UUIDProperty;
import io.swagger.resources.ResourceWithGenerics;
import io.swagger.resources.generics.UserApiRoute;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class GenericsTest {
    private final Swagger swagger = new Reader(new Swagger()).read(ResourceWithGenerics.class);
    private final Set<String> enumValues = Sets.newHashSet(Collections2.transform(Arrays.asList(TestEnum.values()), Functions.toStringFunction()));

    private void testEnumCollection(QueryParameter p, String name) {
        testCollection(p, name, "string", null);
        StringProperty schema = (StringProperty) p.getItems();
        assertEquals(schema.getEnum(), enumValues);
    }

    private void testCollection(QueryParameter p, String name, String type, String format) {
        assertEquals(p.getName(), name);
        assertEquals(p.getType(), "array");
        assertEquals(p.getFormat(), null);
        assertEquals(p.getCollectionFormat(), "multi");
        assertNotEquals(p.getItems(), null);
        Property schema = p.getItems();
        assertEquals(schema.getType(), type);
        assertEquals(schema.getFormat(), format);
    }

    private void testScalar(QueryParameter p, String name, String type, String format) {
        assertEquals(p.getName(), name);
        assertEquals(p.getType(), type);
        assertEquals(p.getFormat(), format);
        assertEquals(p.getCollectionFormat(), null);
        assertEquals(p.getItems(), null);
    }

    private void testGenericType(Operation op, String type) {
        assertEquals(((RefModel) getBodyParameter(op, 0).getSchema()).getSimpleRef(), type);
    }

    private BodyParameter getBodyParameter(Operation op, int index) {
        return (BodyParameter) op.getParameters().get(index);
    }

    private Operation getOperation(String name) {
        return swagger.getPath("/generics/" + name).getPost();
    }

    private QueryParameter getQueryParameter(Operation op, int index) {
        return (QueryParameter) op.getParameters().get(index);
    }

    private Property getProperty(String type, String name) {
        return (swagger.getDefinitions().get(type)).getProperties().get(name);
    }

    @Test(description = "check collections of integers")
    public void checkCollectionsOfIntegers() {
        Operation op = getOperation("testIntegerContainers");
        assertEquals(op.getParameters().size(), 8);
        testCollection(getQueryParameter(op, 0), "set", "integer", "int32");
        testCollection(getQueryParameter(op, 1), "list", "integer", "int32");
        testCollection(getQueryParameter(op, 2), "list2D", "string", null);
        testCollection(getQueryParameter(op, 3), "array", "integer", "int32");
        testCollection(getQueryParameter(op, 4), "arrayP", "integer", "int32");
        testScalar(getQueryParameter(op, 5), "scalar", "integer", "int32");
        testScalar(getQueryParameter(op, 6), "scalarP", "integer", "int32");
        testCollection(getQueryParameter(op, 7), "forced", "integer", "int32");
    }

    @Test(description = "check collections of strings")
    public void checkCollectionsOfStrings() {
        Operation op = getOperation("testStringContainers");
        assertEquals(op.getParameters().size(), 5);
        QueryParameter set = getQueryParameter(op, 0);
        testCollection(set, "set", "string", null);
        assertEquals(((StringProperty) set.getItems()).getEnum(), Arrays.asList("1", "2", "3"));
        testCollection(getQueryParameter(op, 1), "list", "string", null);
        testCollection(getQueryParameter(op, 2), "list2D", "string", null);
        testCollection(getQueryParameter(op, 3), "array", "string", null);
        testScalar(getQueryParameter(op, 4), "scalar", "string", null);
    }

    @Test(description = "check collections of objects")
    public void checkCollectionsOfObjects() {
        Operation op = getOperation("testObjectContainers");
        assertEquals(op.getParameters().size(), 5);
        testCollection(getQueryParameter(op, 0), "set", "string", null);
        testCollection(getQueryParameter(op, 1), "list", "string", null);
        testCollection(getQueryParameter(op, 2), "list2D", "string", null);
        testCollection(getQueryParameter(op, 3), "array", "string", null);
        testScalar(getQueryParameter(op, 4), "scalar", "string", null);
    }

    @Test(description = "check collections of enumerations")
    public void checkCollectionsOfEnumerations() {
        Operation op = getOperation("testEnumContainers");
        assertEquals(op.getParameters().size(), 5);
        testEnumCollection(getQueryParameter(op, 0), "set");
        testEnumCollection(getQueryParameter(op, 1), "list");
        testCollection(getQueryParameter(op, 2), "list2D", "string", null);
        testEnumCollection(getQueryParameter(op, 3), "array");
        QueryParameter scalar = getQueryParameter(op, 4);
        testScalar(scalar, "scalar", "string", null);
        assertEquals(scalar.getEnum(), enumValues);
    }

    @Test(description = "check collection of strings as body parameter")
    public void checkCollectionsOfStringsAsBodyParameter() {
        Operation op = getOperation("testStringsInBody");
        assertEquals(op.getParameters().size(), 1);
        BodyParameter p = getBodyParameter(op, 0);
        ArrayModel strArray = (ArrayModel) p.getSchema();
        assertEquals(strArray.getItems().getType(), "string");
    }

    @Test(description = "check collection of objects as body parameter")
    public void checkCollectionsOfObjectsAsBodyParameter() {
        Operation op = getOperation("testObjectsInBody");
        assertEquals(op.getParameters().size(), 1);
        BodyParameter p = getBodyParameter(op, 0);
        ArrayModel objArray = (ArrayModel) p.getSchema();
        assertEquals(((RefProperty) objArray.getItems()).getSimpleRef(), "Tag");
    }

    @Test(description = "check collection of enumerations as body parameter")
    public void checkCollectionsOfEnumerationsAsBodyParameter() {
        Operation op = getOperation("testEnumsInBody");
        assertEquals(op.getParameters().size(), 1);
        BodyParameter p = getBodyParameter(op, 0);
        ArrayModel enumArray = (ArrayModel) p.getSchema();
        assertEquals(((StringProperty) enumArray.getItems()).getEnum(), enumValues);
    }

    @Test(description = "check 2D array as body parameter")
    public void check2DArrayAsBodyParameter() {
        Operation op = getOperation("test2DInBody");
        assertEquals(op.getParameters().size(), 1);
        BodyParameter p = getBodyParameter(op, 0);
        ArrayModel ddArray = (ArrayModel) p.getSchema();
        assertEquals(((RefProperty) ((ArrayProperty) ddArray.getItems()).getItems()).getSimpleRef(), "Tag");
    }

    @Test(description = "check parameters of generic types")
    public void checkParametersOfGenericTypes() {
        Set<String> genericTypes = new HashSet(Arrays.asList("GenericTypeString", "GenericTypeUUID", "GenericTypeGenericTypeString",
                "RenamedGenericTypeString", "RenamedGenericTypeRenamedGenericTypeString"));
        assertTrue(swagger.getDefinitions().keySet().containsAll(genericTypes));

        Operation opString = getOperation("testGenericType");
        testGenericType(opString, "GenericTypeString");
        Property strValue = getProperty("GenericTypeString", "value");
        assertNotEquals(strValue, null);
        assertEquals(strValue.getClass().getName(), StringProperty.class.getName());

        Operation opUUID = getOperation("testStringBasedGenericType");
        testGenericType(opUUID, "GenericTypeUUID");
        Property uuidValue = getProperty("GenericTypeUUID", "value");
        assertNotEquals(uuidValue, null);
        assertEquals(uuidValue.getClass().getName(), UUIDProperty.class.getName());

        Operation opComplex = getOperation("testComplexGenericType");
        testGenericType(opComplex, "GenericTypeGenericTypeString");
        Property complexValue = getProperty("GenericTypeGenericTypeString", "value");
        assertNotEquals(complexValue, null);
        assertEquals(complexValue.getClass().getName(), RefProperty.class.getName());
        assertEquals(((RefProperty) complexValue).getSimpleRef(), "GenericTypeString");

        Operation opRenamed = getOperation("testRenamedGenericType");
        testGenericType(opRenamed, "RenamedGenericTypeRenamedGenericTypeString");
        Property renamedComplexValue = getProperty("RenamedGenericTypeRenamedGenericTypeString", "value");
        assertNotEquals(renamedComplexValue, null);
        assertTrue(renamedComplexValue instanceof RefProperty);
        assertEquals(((RefProperty) renamedComplexValue).getSimpleRef(), "RenamedGenericTypeString");
    }

    @Test(description = "check generic result")
    public void checkGenericResult() {
        Operation op = swagger.getPath("/generics/testGenericResult").getGet();
        Property schema = op.getResponses().get("200").getSchema();
        assertEquals(schema.getClass().getName(), RefProperty.class.getName());
        assertEquals(((RefProperty) schema).getSimpleRef(), "GenericListWrapperTag");

        Property entries = getProperty("GenericListWrapperTag", "entries");
        assertNotEquals(entries, null);
        assertEquals(entries.getClass().getName(), ArrayProperty.class.getName());

        Property items = ((ArrayProperty) entries).getItems();
        assertEquals(items.getClass().getName(), RefProperty.class.getName());
        assertEquals(((RefProperty) items).getSimpleRef(), "Tag");
    }

    @Test(description = "scan model with Generic Type")
    public void scanModelWithGenericType() {
        final Swagger swagger = new Reader(new Swagger()).read(UserApiRoute.class);
        assertNotNull(swagger);
        final Model userEntity = swagger.getDefinitions().get("UserEntity");
        assertNotNull(userEntity);
        final Map<String, Property> properties = userEntity.getProperties();
        assertEquals(properties.size(), 2);
        assertNotNull(properties.get("id"));
        assertNotNull(properties.get("name"));
    }
}
