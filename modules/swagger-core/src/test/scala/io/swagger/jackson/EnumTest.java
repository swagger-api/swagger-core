package io.swagger.jackson;

import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;

public class EnumTest extends SwaggerTestBase {
  public enum Currency { USA, CANADA }

  public void testEnum() throws Exception {
    ModelResolver modelResolver = new ModelResolver(mapper());    
    ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
 
	  Model model = context.resolve(Currency.class);
    assertNotNull(model);

    // Set<String> names = model.getProperties().keySet();
    // if (names.contains("declaringClass")) {
      // TODO how best to handle this?
      // fail("Enum model should not contain property 'declaringClass', does; properties = " + names);
    // }
  }
}