package io.swagger.v3.jaxrs2;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import io.swagger.v3.jaxrs2.resources.model.NestedBeanParam;
import io.swagger.v3.oas.models.media.IntegerSchema;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.resources.model.ListOfStringsBeanParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;

import static org.testng.Assert.assertEquals;

public class BeanParamTest {

    @Path("/")
    private static class MyBeanParamResource {
        @GET
        public String getWithBeanParam(@BeanParam ListOfStringsBeanParam listOfStringsBean) {
            return "result";
        }

        @GET
        @Path("/nested-param")
        public String getWithNestedBeanParam(@BeanParam NestedBeanParam listOfStringsBean) {
            return "result";
        }

    }

    public static class BeanParamResource {

        @Operation(summary = "Get a list of users", description = "Get a list of users registered in the system") 
        @GET
        @Path("/users")
        public List<User> getUsers(
                @BeanParam UserBeanParam param) {
            return null;
        }
        
        @Operation(summary = "Get a list of users", description = "Get a list of users registered in the system") 
        @POST
        @Path("/users")
        public List<User> getUsers(@BeanParam UserBeanParam param, User user) {
            return null;
        }
        
        @Operation(summary = "Get a list of users", description = "Get a list of users registered in the system") 
        @GET
        @Path("/users/empty")
        public List<User> getUsers(@BeanParam EmptyBeanParam param) {
            return null;
        }
        
        @Operation(summary = "Get a list of users", description = "Get a list of users registered in the system") 
        @POST
        @Path("/users/empty")
        public List<User> getUsersEmpty(@BeanParam EmptyBeanParam param, User user) {
            return null;
        }

        class User {
            public String foo;
        }
        
        class UserBeanParam {
            @QueryParam("msg")
            private String msg;
            
            public String getMsg() {
                return msg;
            }
            
            public void setMsg(String msg) {
                this.msg = msg;
            }
        }
        class EmptyBeanParam {
        }
    }
    
    @Test(description = "check array type of serialized BeanParam containing QueryParams") // tests issue #2466
    public void shouldSerializeTypeParameter() {
        OpenAPI openApi = new Reader(new OpenAPI()).read(MyBeanParamResource.class);
        List<Parameter> getOperationParams = openApi.getPaths().get("/").getGet().getParameters();
        assertEquals(getOperationParams.size(), 1);
        Parameter param = getOperationParams.get(0);
        assertEquals(param.getName(), "listOfStrings");
        Schema<?> schema = param.getSchema();
        // These are the important checks:
        assertEquals(schema.getClass(), ArraySchema.class);
        assertEquals(schema.getItems().getType(), "string");
    }

    @Test(description = "check integer type of nested BeanParam containing a QueryParam") // tests issue #2466
    public void shouldSerializeNestedTypeParameter() {
        OpenAPI openApi = new Reader(new OpenAPI()).read(MyBeanParamResource.class);
        List<Parameter> getOperationParams = openApi.getPaths().get("/nested-param").getGet().getParameters();
        Assert.assertEquals(getOperationParams.size(), 1);

        Parameter queryParam = getOperationParams.get(0);
        Assert.assertEquals(queryParam.getName(), "queryParam");
        Schema<?> schema = queryParam.getSchema();
        Assert.assertEquals(schema.getClass(), IntegerSchema.class);
        Assert.assertEquals(schema.getType(), "integer");
        Assert.assertEquals(((IntegerSchema) schema).getDefault(), 10);
    }

    @Test(description = "Checks parameters and responseBody generation for BeanParam annotation") // tests issue #3871
    public void testBeanParamAnnotation() {
        OpenAPI openAPI = new Reader(new OpenAPI()).read(BeanParamResource.class);
        String yaml = "openapi: 3.0.1\n"
                + "paths:\n"
                + "  /users/empty:\n"
                + "    get:\n"
                + "      summary: Get a list of users\n"
                + "      description: Get a list of users registered in the system\n"
                + "      operationId: getUsers\n"
                + "      responses:\n"
                + "        default:\n"
                + "          description: default response\n"
                + "          content:\n"
                + "            '*/*':\n"
                + "              schema:\n"
                + "                type: array\n"
                + "                items:\n"
                + "                  $ref: '#/components/schemas/User'\n"
                + "    post:\n"
                + "      summary: Get a list of users\n"
                + "      description: Get a list of users registered in the system\n"
                + "      operationId: getUsersEmpty\n"
                + "      requestBody:\n"
                + "        content:\n"
                + "          '*/*':\n"
                + "            schema:\n"
                + "              $ref: '#/components/schemas/User'\n"
                + "      responses:\n"
                + "        default:\n"
                + "          description: default response\n"
                + "          content:\n"
                + "            '*/*':\n"
                + "              schema:\n"
                + "                type: array\n"
                + "                items:\n"
                + "                  $ref: '#/components/schemas/User'\n"
                + "  /users:\n"
                + "    get:\n"
                + "      summary: Get a list of users\n"
                + "      description: Get a list of users registered in the system\n"
                + "      operationId: getUsers_1\n"
                + "      parameters:\n"
                + "      - name: msg\n"
                + "        in: query\n"
                + "        schema:\n"
                + "          type: string\n"
                + "      responses:\n"
                + "        default:\n"
                + "          description: default response\n"
                + "          content:\n"
                + "            '*/*':\n"
                + "              schema:\n"
                + "                type: array\n"
                + "                items:\n"
                + "                  $ref: '#/components/schemas/User'\n"
                + "    post:\n"
                + "      summary: Get a list of users\n"
                + "      description: Get a list of users registered in the system\n"
                + "      operationId: getUsers_2\n"
                + "      parameters:\n"
                + "      - name: msg\n"
                + "        in: query\n"
                + "        schema:\n"
                + "          type: string\n"
                + "      requestBody:\n"
                + "        content:\n"
                + "          '*/*':\n"
                + "            schema:\n"
                + "              $ref: '#/components/schemas/User'\n"
                + "      responses:\n"
                + "        default:\n"
                + "          description: default response\n"
                + "          content:\n"
                + "            '*/*':\n"
                + "              schema:\n"
                + "                type: array\n"
                + "                items:\n"
                + "                  $ref: '#/components/schemas/User'\n"
                + "components:\n"
                + "  schemas:\n"
                + "    User:\n"
                + "      type: object\n"
                + "      properties:\n"
                + "        foo:\n"
                + "          type: string\n"
                + "    EmptyBeanParam:\n"
                + "      type: object";

        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }
}