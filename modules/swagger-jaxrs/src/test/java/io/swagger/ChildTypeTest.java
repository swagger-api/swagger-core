package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.RefModel;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.resources.ResourceWithChildType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * This class test that refs are setup correctly when a child type is referenced.
 */
public class ChildTypeTest {
    private final Swagger swagger = new Reader(new Swagger()).read(ResourceWithChildType.class);

    private BodyParameter getBodyParameter(Operation op, int index) {
        return (BodyParameter) op.getParameters().get(index);
    }

    @Test(description = "Tests child type response schema ref is correctly set up")
    public void testChildTypeResponse() {
        Operation op = swagger.getPath("/childType/testChildTypeResponse").getGet();
        Property schema = op.getResponses().get("200").getSchema();
        assertEquals(schema.getClass().getName(), RefProperty.class.getName());
        assertEquals(((RefProperty) schema).getSimpleRef(), "Sub1Bean");
    }

    @Test(description = "Tests child type response schema ref is correctly set up when specified on the operation")
    public void testChildTypeResponseOnOperation() {
        Operation op = swagger.getPath("/childType/testChildTypeResponseOnOperation").getGet();
        Property schema = op.getResponses().get("200").getSchema();
        assertEquals(schema.getClass().getName(), RefProperty.class.getName());
        assertEquals(((RefProperty) schema).getSimpleRef(), "Sub1Bean");
    }

    @Test(description = "Tests schema ref is correctly set up for child type parameter")
    public void testChildTypeParameter() {
        Operation op = swagger.getPath("/childType/testChildTypeParameter").getPost();
        BodyParameter parameter = getBodyParameter(op, 0);
        Model schema = parameter.getSchema();
        assertEquals(schema.getClass().getName(), RefModel.class.getName());
        assertEquals(((RefModel) schema).getSimpleRef(), "Sub1Bean");
    }

}
