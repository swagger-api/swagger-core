package io.swagger.models;

import org.powermock.reflect.Whitebox;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class SecurityRequirementTest {

    @Test
    public void testScope() {
        //given
        String name = "name";
        SecurityRequirement securityRequirement = new SecurityRequirement(name);
        String scope = "scope";

        //when
        securityRequirement.scope(scope);

        //then
        assertTrue(securityRequirement.getScopes().contains(scope), "The newly added scope must be contained in the scopes list");
    }

    @Test
    public void testRequirement() {
        //given
        SecurityRequirement securityRequirement = new SecurityRequirement();
        Whitebox.setInternalState(securityRequirement, "requirements", (Object) null);

        //when
        String requirement = "requirement";
        securityRequirement.requirement(requirement);

        //then
        assertTrue(securityRequirement.getRequirements().get(requirement).isEmpty(), "Not passing the security requiement must result in an empty requiments list");

        //when
        securityRequirement.setRequirements("other", null);

        //then
        assertNull(securityRequirement.getRequirements().get("other"), "Passing null as requiements must result in anjull requirements list");
    }
}
