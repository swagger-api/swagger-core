package io.swagger.auth;

import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.In;
import io.swagger.models.auth.OAuth2Definition;

import org.testng.annotations.Test;

import java.io.IOException;

public class AuthSerializationTest {

    @Test(description = "it should convert serialize a basic auth model")
    public void testBasicAuth() throws IOException {
        final BasicAuthDefinition auth = new BasicAuthDefinition();
        final String json = "{\"type\":\"basic\"}";
        SerializationMatchers.assertEqualsToJson(auth, json);
    }

    @Test(description = "it should convert serialize a header key model")
    public void testHeaderKeyToJson() throws IOException {
        final ApiKeyAuthDefinition auth = new ApiKeyAuthDefinition()
                .name("api-key")
                .in(In.HEADER);
        final String json = "{\"type\":\"apiKey\",\"name\":\"api-key\",\"in\":\"header\"}";
        SerializationMatchers.assertEqualsToJson(auth, json);
    }

    @Test(description = "it should convert serialize a header key model to yaml")
    public void testHeaderKeyToYaml() throws IOException {
        final ApiKeyAuthDefinition auth = new ApiKeyAuthDefinition()
                .name("api-key")
                .in(In.HEADER);
        final String yaml = "---\n" +
                "type: \"apiKey\"\n" +
                "name: \"api-key\"\n" +
                "in: \"header\"";
        SerializationMatchers.assertEqualsToYaml(auth, yaml);
    }

    @Test(description = "it should convert serialize an oauth2 implicit flow model")
    public void testImplicitAuth() throws IOException {
        final OAuth2Definition auth = new OAuth2Definition()
                .implicit("http://foo.com/authorization");
        final String json = "{\"type\":\"oauth2\",\"authorizationUrl\":\"http://foo.com/authorization\",\"flow\":\"implicit\"}";
        SerializationMatchers.assertEqualsToJson(auth, json);
    }

    @Test(description = "it should convert serialize an oauth2 password flow model")
    public void testPasswordAuth() throws IOException {
        final OAuth2Definition auth = new OAuth2Definition()
                .password("http://foo.com/token");
        final String json = "{\"type\":\"oauth2\",\"tokenUrl\":\"http://foo.com/token\",\"flow\":\"password\"}";
        SerializationMatchers.assertEqualsToJson(auth, json);
    }

    @Test(description = "it should convert serialize an oauth2 application flow model")
    public void testApplicationAuth() throws IOException {
        final OAuth2Definition auth = new OAuth2Definition()
                .application("http://foo.com/token");
        final String json = "{\"type\":\"oauth2\",\"tokenUrl\":\"http://foo.com/token\",\"flow\":\"application\"}";
        SerializationMatchers.assertEqualsToJson(auth, json);
    }

    @Test(description = "it should convert serialize an oauth2 accessCode flow model")
    public void testaAcessCode() throws IOException {
        final OAuth2Definition auth = new OAuth2Definition()
                .accessCode("http://foo.com/authorizationUrl", "http://foo.com/token");
        final String json = "{\n" +
                "   \"type\":\"oauth2\",\n" +
                "   \"authorizationUrl\":\"http://foo.com/authorizationUrl\",\n" +
                "   \"tokenUrl\":\"http://foo.com/token\",\n" +
                "   \"flow\":\"accessCode\"\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(auth, json);
    }

    @Test(description = "it should convert serialize an oauth2 implicit flow model with scopes")
    public void testImplicitWithScopes() throws IOException {
        final OAuth2Definition auth = new OAuth2Definition()
                .implicit("http://foo.com/authorization")
                .scope("email", "read your email");
        final String json = "{\n" +
                "   \"type\":\"oauth2\",\n" +
                "   \"authorizationUrl\":\"http://foo.com/authorization\",\n" +
                "   \"flow\":\"implicit\",\n" +
                "   \"scopes\":{\n" +
                "      \"email\":\"read your email\"\n" +
                "   }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(auth, json);
    }

    @Test(description = "it should convert serialize an oauth2 password flow model with scopes")
    public void testPasswordWithScopes() throws IOException {
        final OAuth2Definition auth = new OAuth2Definition()
                .password("http://foo.com/token")
                .scope("email", "read your email");
        final String json = "{\n" +
                "   \"type\":\"oauth2\",\n" +
                "   \"tokenUrl\":\"http://foo.com/token\",\n" +
                "   \"flow\":\"password\",\n" +
                "   \"scopes\":{\n" +
                "      \"email\":\"read your email\"\n" +
                "   }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(auth, json);
    }

    @Test(description = "it should convert serialize an oauth2 application flow model with scopes")
    public void testApplicationWithScopes() throws IOException {
        final OAuth2Definition auth = new OAuth2Definition()
                .application("http://foo.com/token")
                .scope("email", "read your email");
        final String json = "{\n" +
                "   \"type\":\"oauth2\",\n" +
                "   \"tokenUrl\":\"http://foo.com/token\",\n" +
                "   \"flow\":\"application\",\n" +
                "   \"scopes\":{\n" +
                "      \"email\":\"read your email\"\n" +
                "   }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(auth, json);
    }

    @Test(description = "it should convert serialize an oauth2 accessCode flow model with scopes")
    public void testAccessCodeWithScopes() throws IOException {
        final OAuth2Definition auth = new OAuth2Definition()
                .accessCode("http://foo.com/authorizationUrl", "http://foo.com/token")
                .scope("email", "read your email");
        final String json = "{\n" +
                "   \"type\":\"oauth2\",\n" +
                "   \"authorizationUrl\":\"http://foo.com/authorizationUrl\",\n" +
                "   \"tokenUrl\":\"http://foo.com/token\",\n" +
                "   \"flow\":\"accessCode\",\n" +
                "   \"scopes\":{\n" +
                "      \"email\":\"read your email\"\n" +
                "   }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(auth, json);
    }
}

