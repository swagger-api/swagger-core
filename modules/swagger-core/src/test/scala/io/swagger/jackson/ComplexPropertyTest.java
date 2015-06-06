package io.swagger.jackson;

import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;

public class ComplexPropertyTest extends SwaggerTestBase {
    public void testOuterBean() throws Exception {
        ModelResolver modelResolver = modelResolver();
        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        ;
        Model model = context
                .resolve(OuterBean.class);
        assertNotNull(model);
    }

    static class OuterBean {
        public int counter;
        public InnerBean inner;
    }

  /*
  /**********************************************************
  /* Test methods
  /**********************************************************
   */

    static class InnerBean {
        public int d;
        public int a;
        public int c;
        public int b;
    }
}
