/**
 * Copyright 2021 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
