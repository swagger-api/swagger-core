package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.media.Schema;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class Ticket3348Test extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;

    @BeforeMethod
    public void beforeMethod() {
        modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }

    @Test
    public void testTicket3348() {
        final Schema model = context.resolve(new AnnotatedType(WithObjects.class));
        assertNotNull(model);
        final Map<String, Schema> props = model.getProperties();
        assertEquals(props.size(), 2);
    }

    static class WithObjects  {
        public Object param1;
        public Object param2;
    }
}
