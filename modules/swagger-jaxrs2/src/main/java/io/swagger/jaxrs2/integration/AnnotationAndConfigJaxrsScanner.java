package io.swagger.jaxrs2.integration;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class AnnotationAndConfigJaxrsScanner extends AnnotationJaxrsScanner<AnnotationAndConfigJaxrsScanner>{

    private Application app;
    private ServletConfig sc;

    public AnnotationAndConfigJaxrsScanner application (Application app) {
        this.app = app;
        return this;
    }

    public AnnotationAndConfigJaxrsScanner servletConfig (ServletConfig s1c) {
        this.sc = sc;
        return this;
    }
    @Override
    public Set<Class<?>> classes() {
        Set<Class<?>> classes = super.classes();
        Set<Class<?>> output = new HashSet<Class<?>>();
        if (app != null) {
            Set<Class<?>> clz = app.getClasses();
            if (clz != null) {
                output.addAll(clz);
            }
            Set<Object> singletons = app.getSingletons();
            if (singletons != null) {
                for (Object o : singletons) {
                    output.add(o.getClass());
                }
            }
        }
        classes.addAll(output);
        return classes;
    }

}
