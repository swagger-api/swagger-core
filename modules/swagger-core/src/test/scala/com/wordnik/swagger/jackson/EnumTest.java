package com.fasterxml.jackson.module.swagger;

import com.wordnik.swagger.jackson.*;
import com.wordnik.swagger.models.*;

import java.util.Set;

public class EnumTest extends SwaggerTestBase {
  public enum Currency { USA, CANADA }

  public void testEnum() throws Exception {
    Model model = new ModelResolver(mapper())
      .resolve(Currency.class);
    assertNotNull(model);
    
    Set<String> names = model.getProperties().keySet();
    if (names.contains("declaringClass")) {
      // TODO how best to handle this?
      // fail("Enum model should not contain property 'declaringClass', does; properties = " + names);
    }
  }
}