package com.wordnik.swagger.sample;

import com.wordnik.swagger.jaxrs.*;
import com.wordnik.swagger.config.*;
import com.wordnik.swagger.model.*;

import javax.servlet.http.HttpServlet;

public class Bootstrap extends HttpServlet {
  static {
    // do any additional initialization here, such as set your base path programmatically as such:
    // ConfigFactory.config().setBasePath("http://www.foo.com/");
  }
}
