package com.wordnik.swagger.sample.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.validation.Valid;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.sun.jersey.api.NotFoundException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.Authorization;
import com.wordnik.swagger.annotations.AuthorizationScope;
import com.wordnik.swagger.models.Contact;
import com.wordnik.swagger.models.ExternalDocs;
import com.wordnik.swagger.models.Info;
import com.wordnik.swagger.models.License;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.Tag;
import com.wordnik.swagger.models.auth.ApiKeyAuthDefinition;
import com.wordnik.swagger.models.auth.In;
import com.wordnik.swagger.models.auth.OAuth2Definition;
import com.wordnik.swagger.sample.model.Pet;
import com.wordnik.swagger.servlet.listing.ApiDeclarationServlet;

@Api(value = "/pet/{petId}", description = "Operatioooons about pets", authorizations = {
  @Authorization(value = "petstore_auth", scopes = {
    @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
    @AuthorizationScope(scope = "read:pets", description = "read your pets")
  })
}, tags = "pet")
public class SampleServlet extends HttpServlet {
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    Info info = new Info()
      .version("1.0.0")
      .title("Swagger Sample App")
      .description(
        "This is a sample server Petstore server.  You can find out more about Swagger "
          + "at <a href=\"http://swagger.io\">http://swagger.io</a> or on irc.freenode.net, #swagger.  For this sample, "
          + "you can use the api key \"special-key\" to test the authorization filters")
      .termsOfService("http://helloreverb.com/terms/").contact(new Contact().email("apiteam@swagger.io"))
      .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"));

    Swagger swagger = new Swagger().info(info);
    swagger.securityDefinition("api_key", new ApiKeyAuthDefinition("api_key", In.HEADER));
    swagger.securityDefinition(
      "petstore_auth",
      new OAuth2Definition().implicit("http://petstore.swagger.io/api/oauth/dialog")
        .scope("read:pets", "read your pets").scope("write:pets", "modify pets in your account"));
    swagger.tag(new Tag().name("pet").description("Everything about your Pets")
      .externalDocs(new ExternalDocs("Find out more", "http://swagger.io")));
    swagger.tag(new Tag().name("store").description("Operations about user"));
    swagger.tag(new Tag().name("user").description("Access to Petstore orders")
      .externalDocs(new ExternalDocs("Find out more about our store", "http://swagger.io")));

    ApiDeclarationServlet.init(swagger);
  }

  @ApiOperation(value = "Find pet by ID",
    notes = "Returns a pet when ID < 10.  ID > 10 or nonintegers will simulate API error conditions",
    response = Pet.class,
    httpMethod = "get",

    authorizations = @Authorization(value = "api_key")
    )
    @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Invalid ID supplied"),
      @ApiResponse(code = 404, message = "Pet not found") })
    @ApiImplicitParams(value = {
      @ApiImplicitParam(value = "ID of pet that needs to be fetched", allowableValues = "range[1,5]", required = true,
        paramType = "path", name = "petId", dataType = "int"),
      @ApiImplicitParam(value = "Dummy to test multiple params", allowableValues = "range[1,5]", required = true,
        paramType = "path", name = "petDummy", dataType = "string")
    })
    public void getPetById() {
    return;
  }
}
