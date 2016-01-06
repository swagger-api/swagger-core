package io.swagger.models.auth;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class AbstractSecuritySchemeDefinitionTest {

    /*
     * Tests getters and setters methods on {@link AbstractSecuritySchemeDefinition}
     * It was not possible to cove it with {@link io.swagger.PojosTest} so a manual implementation is provided for now
     * TODO improve PojosTest to test getters and setters for abstracts classes
     */
    @Test
    public void testGettersAndSetters() {
        // given
        AbstractSecuritySchemeDefinition instance = new ApiKeyAuthDefinition();

        final String vendorName = "x-vendor";
        final String value = "value";

        //when
        instance.setVendorExtension(vendorName, value);

        //then
        assertEquals(instance.getVendorExtensions().get(vendorName), value, "Must be able to retrieve the same value from the map");

        //given
        String description = "description";

        //when
        instance.setDescription(description);

        //then
        assertEquals(instance.getDescription(), description, "The get description must be the same as the set one");
    }
}
