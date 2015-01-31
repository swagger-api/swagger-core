package com.wordnik.swagger.sample;

import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.models.Swagger;

import com.wordnik.swagger.models.auth.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class Bootstrap extends HttpServlet {
  @Override
  public void init(ServletConfig config) throws ServletException {
    ApiInfo info = new ApiInfo(
      "Swagger Sample App",                             /* title */
      "This is a sample server Petstore server.  You can find out more about Swagger " + 
      "at <a href=\"http://swagger.wordnik.com\">http://swagger.wordnik.com</a> or on irc.freenode.net, #swagger.  For this sample, " + 
      "you can use the api key \"special-key\" to test the authorization filters", 
      "http://helloreverb.com/terms/",                  /* TOS URL */
      "apiteam@wordnik.com",                            /* Contact */
      "Apache 2.0",                                     /* license */
      "http://www.apache.org/licenses/LICENSE-2.0.html" /* license URL */
    );

    ServletContext context = config.getServletContext();
    Swagger swagger = new Swagger().info(info);
    swagger.securityDefinition("api_key", new ApiKeyAuthDefinition("api_key", In.HEADER));
    swagger.securityDefinition("petstore_auth", 
      new OAuth2Definition()
        .implicit("http://petstore.swagger.wordnik.com/api/oauth/dialog")
        .scope("read:pets", "read your pets")
        .scope("write:pets", "modify pets in your account"));
    context.setAttribute("swagger", swagger);
  }
}
