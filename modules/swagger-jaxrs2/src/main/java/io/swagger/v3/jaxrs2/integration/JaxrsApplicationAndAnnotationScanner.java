package io.swagger.v3.jaxrs2.integration;

import java.util.HashSet;
import java.util.Set;

public class JaxrsApplicationAndAnnotationScanner extends JaxrsAnnotationScanner<JaxrsApplicationAndAnnotationScanner> {

    @Override
    public Set<Class<?>> classes() {
        Set<Class<?>> classes = super.classes();
        Set<Class<?>> output = new HashSet<Class<?>>();
        if (application != null) {
            Set<Class<?>> clzs = application.getClasses();
            if (clzs != null) {
                for (Class<?> clz : clzs) {
                    if (!isIgnored(clz.getName())) {
                        output.add(clz);
                    }
                }
            }
            Set<Object> singletons = application.getSingletons();
            if (singletons != null) {
                for (Object o : singletons) {
                    if (!isIgnored(o.getClass().getName())) {
                        output.add(o.getClass());
                    }
                }
            }
        }
        classes.addAll(output);
        return classes;
    }
}
