package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.resources.petresource.EmptyPetResource;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Pet Resource Test Class
 * Adding a lot of tests of different pet resource examples
 * Created by rafaellopez on 11/9/17.
 */
public class PetResourceTest {

    private static final String OPEN_API_VERSION = "3.0.0";

    @Test(description = "Test an empty resource class (Without operations or annotations)")
    public void testSimpleReadClass() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(EmptyPetResource.class);
        assertNotNull(openAPI);
        assertEquals(openAPI.getOpenapi(), OPEN_API_VERSION);

    }
}
