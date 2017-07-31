package io.swagger.jaxrs2.integration;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class JaxrsApplicationScanner extends JaxrsAnnotationScanner<JaxrsApplicationScanner> {

    private Application app;

    public JaxrsApplicationScanner application (Application app) {
        this.app = app;
        return this;
    }

    @Override
    public Set<Class<?>> classes() {
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
        return output;
    }

}
