package com.wordnik.swagger.sample;

import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.models.*;
// import com.wordnik.swagger.config.*;
import com.wordnik.swagger.sample.util.*;
// import com.wordnik.swagger.config.FilterFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import java.util.List;
import java.util.ArrayList;

public class Bootstrap extends HttpServlet {
  static Swagger swagger;
  static Info info;
  static {
    /*
    BeanConfig bc = new BeanConfig();

    bc.setResourcePackage("com.wordnik.swagger.sample.resource");

    bc.setHost("petstore.swagger.wordnik.com");
    bc.setBasePath("/api");
    bc.setVersion("1.0.0");
    bc.setTitle("Petstore Sample API");
    bc.setDescription("A sample API that uses a petstore as an example to demonstrate features in the swagger-2.0 specification");
    bc.setTermsOfServiceUrl("http://helloreverb.com/terms/");
    bc.setContact("Wordnik API Team");
    bc.setLicense("MIT");
    bc.setLicenseUrl("http://github.com/gruntjs/grunt/blob/master/LICENSE-MIT");
*/


    info = new Info()
      .title("Swagger Sample App")
      .description("This is a sample server Petstore server.  You can find out more about Swagger " + 
        "at <a href=\"http://swagger.wordnik.com\">http://swagger.wordnik.com</a> or on irc.freenode.net, #swagger.  For this sample, " + 
        "you can use the api key \"special-key\" to test the authorization filters")
      .termsOfService("http://helloreverb.com/terms/")                  /* TOS URL */
      .contact(new Contact()
        .name("Wordnik API Team")
        .email("apiteam@wordnik.com"))
      .license(new License()
        .name("MIT")
        .url("http://github.com/gruntjs/grunt/blob/master/LICENSE-MIT"));

    // bc.setScan(true);

    // swagger = bc.getSwagger();

    // do any additional initialization here, such as set your base path programmatically as such:
    // ConfigFactory.config().setBasePath("http://www.foo.com/");


    // ApiInfo info = new ApiInfo(
    //   "Swagger Sample App",                             /* title */
    //   "This is a sample server Petstore server.  You can find out more about Swagger " + 
    //   "at <a href=\"http://swagger.wordnik.com\">http://swagger.wordnik.com</a> or on irc.freenode.net, #swagger.  For this sample, " + 
    //   "you can use the api key \"special-key\" to test the authorization filters", 
    //   "http://helloreverb.com/terms/",                  /* TOS URL */
    //   "apiteam@wordnik.com",                            /* Contact */
    //   "Apache 2.0",                                     /* license */
    //   "http://www.apache.org/licenses/LICENSE-2.0.html"  license URL 
    // );

    // List<AuthorizationScope> scopes = new ArrayList<AuthorizationScope>();
    // scopes.add(new AuthorizationScope("email", "Access to your email address"));
    // scopes.add(new AuthorizationScope("pets", "Access to your pets"));

    // List<GrantType> grantTypes = new ArrayList<GrantType>();

    // ImplicitGrant implicitGrant = new ImplicitGrant(
    //   new LoginEndpoint("http://localhost:8002/oauth/dialog"), 
    //   "access_code");

    // grantTypes.add(implicitGrant);

    // AuthorizationType oauth = new OAuthBuilder().scopes(scopes).grantTypes(grantTypes).build();

    // ConfigFactory.config().addAuthorization(oauth);
    // ConfigFactory.config().setApiInfo(info);
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    ServletContext context = config.getServletContext();
    Swagger swagger = new Swagger().info(info);
    context.setAttribute("swagger", swagger);
  }
}
