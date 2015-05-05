package com.wordnik.swagger.servlet.listing;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wordnik.swagger.config.Scanner;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.jaxrs.listing.SwaggerSerializers;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.servlet.ServletReader;
import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.util.Yaml;

public class ApiDeclarationServlet extends HttpServlet {
  private static final Logger LOGGER = LoggerFactory.getLogger(ApiDeclarationServlet.class);
  static boolean initialized = false;
  static Swagger swagger;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    scan();
  }

  public static void init(Swagger swagger) {
    ApiDeclarationServlet.swagger = swagger;
  }

  protected synchronized void scan() {
    Scanner scanner = ScannerFactory.getScanner();
    LOGGER.debug("using scanner " + scanner);
    if (scanner != null) {
      SwaggerSerializers.setPrettyPrint(scanner.getPrettyPrint());
      Set<Class<?>> classes = null;
      classes = scanner.classes();
      if (classes != null) {
        ServletReader reader = new ServletReader(swagger);
        swagger = reader.read(classes);
      }
    }
    initialized = true;
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse response)
    throws ServletException, IOException {
    if (req.getRequestURI().equals("/swagger.json")) {
      if (swagger != null) {
        try {
          response.getWriter().println(Json.mapper().writeValueAsString(swagger));
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      }
    } else if (req.getRequestURI().equals("/swagger.yaml")) {
      try {
        response.getWriter().println(Yaml.mapper().writeValueAsString(swagger));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
  }
}
