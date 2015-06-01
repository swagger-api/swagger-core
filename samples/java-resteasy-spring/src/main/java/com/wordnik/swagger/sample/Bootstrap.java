package com.wordnik.swagger.sample;

import com.wordnik.swagger.models.Contact;
import com.wordnik.swagger.models.ExternalDocs;
import com.wordnik.swagger.models.Info;
import com.wordnik.swagger.models.License;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.Tag;
import com.wordnik.swagger.models.auth.OAuth2Definition;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


public class Bootstrap extends HttpServlet {

  @Override
  public void init(ServletConfig config) throws ServletException {
    Info info = new Info()
            .title("Swagger Sample App")
            .description("This is a sample server Petstore server.  You can find out more about Swagger " +
                    "at <a href=\"http://swagger.io\">http://swagger.io</a> or on irc.freenode.net, #swagger.  For this sample, " +
                    "you can use the api key \"special-key\" to test the authorization filters")
            .termsOfService("http://helloreverb.com/terms/")
            .contact(new Contact()
                    .email("apiteam@swagger.io"))
            .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

    ServletContext context = config.getServletContext();
    Swagger swagger = new Swagger().info(info);
    swagger.securityDefinition("petstore_auth",
            new OAuth2Definition()
                    .implicit("http://localhost:8002/oauth/dialog")
                    .scope("email", "Access to your email address")
                    .scope("pets", "Access to your pets"));
    swagger.tag(new Tag()
            .name("pet")
            .description("Everything about your Pets")
            .externalDocs(new ExternalDocs("Find out more", "http://swagger.io")));
    swagger.tag(new Tag()
            .name("store")
            .description("Access to Petstore orders"));
    swagger.tag(new Tag()
            .name("user")
            .description("Operations about user")
            .externalDocs(new ExternalDocs("Find out more about our store", "http://swagger.io")));
    context.setAttribute("swagger", swagger);
  }
}
