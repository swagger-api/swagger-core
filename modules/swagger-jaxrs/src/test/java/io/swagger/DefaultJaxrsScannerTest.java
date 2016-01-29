package io.swagger;

import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import org.testng.annotations.Test;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class DefaultJaxrsScannerTest {

    @Test(description = "should return singletones")
    public void classesFromContextTest() {
        Application app = mock(Application.class);
        Set<Object> singletons = new HashSet<Object>(asList(new io.swagger.jaxrs.listing.ApiListingResource(), new DefaultJaxrsScannerTest()));

        when(app.getClasses()).thenReturn(null);
        when(app.getSingletons()).thenReturn(singletons);

        DefaultJaxrsScanner scanner = new DefaultJaxrsScanner();
        Set<Class<?>> output = scanner.classesFromContext(app, null);

        Set<Class<?>> classes = new HashSet<Class<?>>();
        for (final Object o : singletons) {
            classes.add(o.getClass());
        }

        assertEquals(output, classes);
    }
}
