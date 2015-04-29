package com.wordnik.swagger.servlet.listing;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.wordnik.swagger.jaxrs.Reader;
import com.wordnik.swagger.jaxrs.listing.SwaggerSerializers;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.util.Json;

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
        Reader reader = new Reader(swagger);
        swagger = reader.read(classes);
        // This loads the Swagger root information from the mule-swagger-integration.xml instead of
        // the Bootstrap initializer. Should I leave both ways in, or support only one ? The new API doesn't
        // seem work very well in the bean xml syntax

        // swagger = ((SwaggerConfig) scanner).configure(swagger);
      }
    }
    initialized = true;
  }

  ////  @Path("/swagger.json")
  //  public Response getListingJson(@Context Application app, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
  //    // On first use scan the API and initialize Swagger
  //    if (!initialized)
  //      scan(app);
  //
  //    if (swagger != null) {
  //      try {
  //        return Response.ok().entity(Json.mapper().writeValueAsString(swagger)).build();
  //      } catch (JsonProcessingException e) {
  //        // This should probably be logged in some project specific way but I couldn't find a standard way it's done
  //        e.printStackTrace();
  //        return Response.status(405).build();
  //      }
  //    } else return Response.status(404).build(); // This was a 404 in the example, but it seems more like a 405
  //  }
  //
  //  //  @Path("/swagger.yaml")
  //  public Response getListingYaml(@Context Application app, @Context HttpHeaders headers, @Context UriInfo uriInfo) {
  //    // On first use scan the API and initialize Swagger
  //    if (!initialized)
  //      scan(app);
  //
  //    if (swagger != null) {
  //      try {
  //        String yaml = Yaml.mapper().writeValueAsString(swagger);
  //        return Response.ok().entity(yaml).type("text/plain").build();
  //      } catch (JsonProcessingException e) {
  //        // This should probably be logged in some project specific way but I couldn't find a standard way it's done
  //        e.printStackTrace();
  //        return Response.status(405).build();
  //      }
  //    } else return Response.status(404).build(); // This was a 404 in the example, but it seems more like a 405
  //  }
  //  
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
    throws ServletException, IOException {
    System.out.println("##########" + req.getRequestURI() + "|" + req.getServletPath());
    if (req.getRequestURI().toLowerCase().equals("/swagger.json")) {
      if (swagger != null) {
        try {
          System.out.println("$############$$$$$$$$$$ " + Json.mapper().writeValueAsString(swagger));
          response.getWriter().println(Json.mapper().writeValueAsString(swagger));
          return;
        } catch (JsonProcessingException e) {
          // This should probably be logged in some project specific way but I couldn't find a standard way it's done
          e.printStackTrace();
          return;
        }
      }
    } else {
      PrintWriter out = response.getWriter();
      out.println("{\"apiVersion\":\"1.0.0\",\"swaa123aaggerVersion\":\"1.2\",\"apis\":[{\"path\":\"/sample\"}]}");
      out.flush();
      out.close();
    }
  }
}
