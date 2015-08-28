package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.resources.PostParamResource;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class PostParamTest {
    private static final String BODY = "body";
    private static final String PET = "Pet";
    private final Swagger swagger = new Reader(new Swagger()).read(PostParamResource.class);

    @Test(description = "find a Post operation with single object")
    public void findPostOperationWithSingleObject() {
        Path petPath = getPath("singleObject");
        assertNotNull(petPath);
        assertNull(petPath.getGet());
        Operation petPost = petPath.getPost();
        assertNotNull(petPost);
        assertEquals(petPost.getParameters().size(), 1);
        BodyParameter petPostBodyParam = (BodyParameter) petPost.getParameters().get(0);
        assertEquals(petPostBodyParam.getName(), BODY);

        assertTrue(petPostBodyParam.getSchema() instanceof Model);
        assertEquals(swagger.getDefinitions().get(PET).getProperties().get("status").getAccess(), "public");
    }

    @Test(description = "find a Post operation with list of objects")
    public void findPostOperationWithObjectsList() {
        Path petPath = getPath("listOfObjects");
        assertNotNull(petPath);
        Operation petPost = petPath.getPost();
        assertNotNull(petPost);
        assertEquals(petPost.getParameters().size(), 1);
        BodyParameter petPostBodyParam = (BodyParameter) petPost.getParameters().get(0);
        assertEquals(petPostBodyParam.getName(), BODY);

        Model inputModel = petPostBodyParam.getSchema();
        assertTrue(inputModel instanceof ArrayModel);
        ArrayModel ap = (ArrayModel) inputModel;
        Property inputSchema = ap.getItems();
        assertTrue(inputSchema instanceof RefProperty);
        RefProperty rm = (RefProperty) inputSchema;
        assertEquals(rm.getSimpleRef(), PET);
    }

    @Test(description = "find a Post operation with collection of objects")
    public void findPostOperationWithObjectsCollection() {
        Path petPath = getPath("collectionOfObjects");
        assertNotNull(petPath);
        Operation petPost = petPath.getPost();
        assertNotNull(petPost);
        assertEquals(petPost.getParameters().size(), 1);
        BodyParameter petPostBodyParam = (BodyParameter) petPost.getParameters().get(0);
        assertEquals(petPostBodyParam.getName(), BODY);

        Model inputModel = petPostBodyParam.getSchema();
        assertTrue(inputModel instanceof ArrayModel);
        ArrayModel ap = (ArrayModel) inputModel;
        Property inputSchema = ap.getItems();
        assertTrue(inputSchema instanceof RefProperty);
        RefProperty rm = (RefProperty) inputSchema;
        assertEquals(rm.getSimpleRef(), PET);
    }

    @Test(description = "find a Post operation with an array of objects")
    public void findAPostOperationWithObjectsArray() {
        Path petPath = getPath("arrayOfObjects");
        assertNotNull(petPath);
        Operation petPost = petPath.getPost();
        assertNotNull(petPost);
        assertEquals(petPost.getParameters().size(), 1);
        BodyParameter petPostBodyParam = (BodyParameter) petPost.getParameters().get(0);
        assertEquals(petPostBodyParam.getName(), BODY);

        Model inputModel = petPostBodyParam.getSchema();
        assertTrue(inputModel instanceof ArrayModel);
        ArrayModel ap = (ArrayModel) inputModel;
        Property inputSchema = ap.getItems();
        assertTrue(inputSchema instanceof RefProperty);
        RefProperty rm = (RefProperty) inputSchema;
        assertEquals(rm.getSimpleRef(), PET);
    }

    @Test(description = "find a Post operation with single string")
    public void findAPostOperationWithSingleString() {
        Path petPath = getPath("singleString");
        assertNotNull(petPath);
        assertNull(petPath.getGet());
        Operation petPost = petPath.getPost();
        assertNotNull(petPost);
        assertEquals(petPost.getParameters().size(), 1);
        BodyParameter petPostBodyParam = (BodyParameter) petPost.getParameters().get(0);
        assertEquals(petPostBodyParam.getName(), BODY);

        assertTrue(petPostBodyParam.getSchema() instanceof Model);
    }

    @Test(description = "find a Post operation with list of strings")
    public void findAPostOperationWithStringsList() {
        Path petPath = getPath("listOfStrings");
        assertNotNull(petPath);
        Operation petPost = petPath.getPost();
        assertNotNull(petPost);
        assertEquals(petPost.getParameters().size(), 1);
        BodyParameter petPostBodyParam = (BodyParameter) petPost.getParameters().get(0);
        assertEquals(petPostBodyParam.getName(), BODY);

        Model inputModel = petPostBodyParam.getSchema();
        assertTrue(inputModel instanceof ArrayModel);
        ArrayModel ap = (ArrayModel) inputModel;
        Property inputSchema = ap.getItems();
        assertTrue(inputSchema instanceof StringProperty);
    }

    @Test(description = "find a Post operation with collection of strings")
    public void findAPostOperationWithStringsCollection() {
        Path petPath = getPath("collectionOfStrings");
        assertNotNull(petPath);
        Operation petPost = petPath.getPost();
        assertNotNull(petPost);
        assertEquals(petPost.getParameters().size(), 1);
        BodyParameter petPostBodyParam = (BodyParameter) petPost.getParameters().get(0);
        assertEquals(petPostBodyParam.getName(), BODY);

        Model inputModel = petPostBodyParam.getSchema();
        assertTrue(inputModel instanceof ArrayModel);
        ArrayModel ap = (ArrayModel) inputModel;
        Property inputSchema = ap.getItems();
        assertTrue(inputSchema instanceof StringProperty);
    }

    @Test(description = "find a Post operation with an array of strings")
    public void findAPostOperationWithStringsArray() {
        Path petPath = getPath("arrayOfStrings");
        assertNotNull(petPath);
        Operation petPost = petPath.getPost();
        assertNotNull(petPost);
        assertEquals(petPost.getParameters().size(), 1);
        BodyParameter petPostBodyParam = (BodyParameter) petPost.getParameters().get(0);
        assertEquals(petPostBodyParam.getName(), BODY);

        Model inputModel = petPostBodyParam.getSchema();
        assertTrue(inputModel instanceof ArrayModel);
        ArrayModel ap = (ArrayModel) inputModel;
        Property inputSchema = ap.getItems();
        assertTrue(inputSchema instanceof StringProperty);
    }

    private Path getPath(String path) {
        return swagger.getPaths().get("/pet/".concat(path));
    }
}
