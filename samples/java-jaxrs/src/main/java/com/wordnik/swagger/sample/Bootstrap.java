package com.wordnik.swagger.sample;

import com.wordnik.swagger.models.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class Bootstrap extends HttpServlet {
  @Override
  public void init(ServletConfig config) throws ServletException {
    Info info = new Info()
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

    ServletContext context = config.getServletContext();
    Swagger swagger = new Swagger().info(info);
    context.setAttribute("swagger", swagger);
  }
}
