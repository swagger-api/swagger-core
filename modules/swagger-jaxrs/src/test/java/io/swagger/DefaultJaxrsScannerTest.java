package io.swagger;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import org.testng.annotations.Test;

import javax.ws.rs.core.Application;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class DefaultJaxrsScannerTest {

    @Test(description = "should return singletones")
    public void classesFromContextTest() {
        Application app = mock(Application.class);
        Set<Object> singletons = Sets.newHashSet(new io.swagger.jaxrs.listing.ApiListingResource(), new DefaultJaxrsScannerTest());

        when(app.getClasses()).thenReturn(null);
        when(app.getSingletons()).thenReturn(singletons);

        DefaultJaxrsScanner scanner = new DefaultJaxrsScanner();
        Set<Class<?>> output = scanner.classesFromContext(app, null);

        Set<Class<?>> classes = Sets.newHashSet(Iterables.transform(singletons, new Function<Object, Class<?>>() {
            @Override
            public Class<?> apply(Object o) {
                return o.getClass();
            }
        }));

        assertEquals(output, classes);
    }
}
