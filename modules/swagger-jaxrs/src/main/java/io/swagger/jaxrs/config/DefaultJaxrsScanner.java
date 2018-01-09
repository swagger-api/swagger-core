package io.swagger.jaxrs.config;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class DefaultJaxrsScanner extends AbstractScanner implements JaxrsScanner {
    @Override
    public Set<Class<?>> classesFromContext(Application app, ServletConfig sc) {
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

    @Override
    public Set<Class<?>> classes() {
        return new HashSet<Class<?>>();
    }

    public boolean prettyPrint() {
        return getPrettyPrint();
    }
}
