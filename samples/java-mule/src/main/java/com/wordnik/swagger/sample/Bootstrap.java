package com.wordnik.swagger.sample;

import com.sun.jersey.spi.resource.Singleton;
import com.wordnik.swagger.models.Contact;
import com.wordnik.swagger.models.ExternalDocs;
import com.wordnik.swagger.models.Info;
import com.wordnik.swagger.models.License;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.Tag;
import com.wordnik.swagger.models.auth.ApiKeyAuthDefinition;
import com.wordnik.swagger.models.auth.In;
import com.wordnik.swagger.models.auth.OAuth2Definition;
import com.wordnik.swagger.mule.ApiListingJSON;

@Singleton
public class Bootstrap {
  /** Run on app init by Spring */
  public void start() {
    Info info = new Info()
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

    ApiListingJSON.init(swagger);
  }
}
