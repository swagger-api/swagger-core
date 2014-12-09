package com.fasterxml.jackson.module.swagger;

import com.wordnik.swagger.jackson.*;
import com.wordnik.swagger.models.*;

public class ComplexPropertyTest extends SwaggerTestBase {
  static class OuterBean {
    public int counter;
    public InnerBean inner;
  }

  static class InnerBean {
    public int d;
    public int a;
    public int c;
    public int b;
  }

  /*
  /**********************************************************
  /* Test methods
  /**********************************************************
   */

  public void testOuterBean() throws Exception  {
    Model model = modelResolver()
      .resolve(OuterBean.class);
    assertNotNull(model);
  }
}
