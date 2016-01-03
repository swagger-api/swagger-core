package io.swagger.models.auth;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ApiKeyAuthDefinitionTest {

    @Test
    public void testConstructor() {
        // given
        final String name = "name";

        //when
        ApiKeyAuthDefinition apiKeyAuthDefinition = new ApiKeyAuthDefinition(name, In.HEADER);

        //then
        assertEquals(apiKeyAuthDefinition.getName(), name, "The getName() must return the same as the one passed to the constructor");
        assertEquals(apiKeyAuthDefinition.getIn(), In.HEADER, "The getIn() must return the same as the one passed to the constructor");
    }
}
