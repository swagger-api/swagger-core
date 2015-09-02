package io.swagger.servlet.extensions;

import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.servlet.ReaderContext;
import io.swagger.servlet.resources.ResourceWithAnnotations;
import io.swagger.servlet.resources.ResourceWithoutApiAnnotation;

import java.lang.reflect.Method;
import java.util.Collections;

public abstract class BaseServletReaderExtensionTest {

    protected static final ReaderExtension extension = new ServletReaderExtension();

    protected static ReaderContext createDefaultContext() {
        return createContext(ResourceWithAnnotations.class);
    }

    protected static ReaderContext createDefaultContextWithoutApi() {
        return createContext(ResourceWithoutApiAnnotation.class);
    }

    protected static ReaderContext createContext(Class<?> cls) {
        return new ReaderContext(new Swagger(), cls, "", null, false, Collections.<String>emptyList(),
                Collections.<String>emptyList(), Collections.<String>emptyList(), Collections.<Parameter>emptyList());
    }

    protected static Method findMethod(ReaderContext context, String methodName) throws NoSuchMethodException {
        return context.getCls().getMethod(methodName);
    }
}
