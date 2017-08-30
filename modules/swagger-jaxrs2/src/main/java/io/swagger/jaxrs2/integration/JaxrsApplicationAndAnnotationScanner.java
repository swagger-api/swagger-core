package io.swagger.jaxrs2.integration;

import java.util.HashSet;
import java.util.Set;

public class JaxrsApplicationAndAnnotationScanner extends JaxrsAnnotationScanner<JaxrsApplicationAndAnnotationScanner> {


    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = super.getClasses();
        Set<Class<?>> output = new HashSet<Class<?>>();
        if (application != null) {
            Set<Class<?>> clz = application.getClasses();
            if (clz != null) {
                output.addAll(clz);
            }
            Set<Object> singletons = application.getSingletons();
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
