package io.swagger.models.auth;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class OAuth2DefinitionTest {

    private OAuth2Definition auth2Definition = new OAuth2Definition();

    private String tokenUrl = "tokenUrl";

    private String authorizationUrl = "authorizationUrl";

    private String name = "name";

    private String description = "description";

    @Test
    public void testImplicit() {
        //when
        auth2Definition.implicit(authorizationUrl);

        //then
        assertEquals(auth2Definition.getAuthorizationUrl(), authorizationUrl, "The get authorization must be the same as the set one");
        assertEquals(auth2Definition.getFlow(), "implicit", "Flow must be implicit after calling implicit()");
    }

    @Test
    public void testPassword() {
        //when
        auth2Definition.password(tokenUrl);

        //then
        assertEquals(auth2Definition.getTokenUrl(), tokenUrl, "The getTokenUrl must be the same as the set one");
        assertEquals(auth2Definition.getFlow(), "password", "Flow must be password after calling password()");
    }

    @Test
    public void testApplication() {
        //when
        auth2Definition.application(tokenUrl);

        //then
        assertEquals(auth2Definition.getTokenUrl(), tokenUrl, "The getTokenUrl must be the same as the set one");
        assertEquals(auth2Definition.getFlow(), "application", "Flow must be application after calling application()");
    }

    @Test
    public void testAccessCode() {
        //when
        auth2Definition.accessCode(authorizationUrl, tokenUrl);

        //then
        assertEquals(auth2Definition.getTokenUrl(), tokenUrl, "The getTokenUrl must be the same as the set one");
        assertEquals(auth2Definition.getAuthorizationUrl(), authorizationUrl, "The get authorizationUrl must be the same as the set one");
        assertEquals(auth2Definition.getFlow(), "accessCode", "Flow must be accessCode after calling accessCode()");
    }

    @Test
    public void testScope() {
        //when
        auth2Definition.scope(name, description);

        //then
        assertEquals(auth2Definition.getScopes().get(name), description, "Must be able to retrieve the set scope");
    }
}
