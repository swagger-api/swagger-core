package io.swagger.jackson;

import static org.testng.Assert.assertNotNull;

import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;

import org.testng.annotations.Test;

public class ComplexPropertyTest extends SwaggerTestBase {

    @Test
    public void testOuterBean() throws Exception {
        final ModelResolver modelResolver = modelResolver();
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        final Model model = context
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
