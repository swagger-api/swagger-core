package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class ComplexPropertyTest extends SwaggerTestBase {

    @Test
    public void testOuterBean() throws Exception {
        final ModelResolver modelResolver = modelResolver();
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
        final Schema model = context
                .resolve(new AnnotatedType(OuterBean.class));
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
