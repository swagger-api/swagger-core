package io.swagger.v3.jaxrs2.integration;

import java.util.HashSet;
import java.util.Set;

/**
 * @since 2.0.10
 */
public class JaxrsApplicationAndResourcePackagesAnnotationScanner extends JaxrsAnnotationScanner<JaxrsApplicationAndResourcePackagesAnnotationScanner> {

    public JaxrsApplicationAndResourcePackagesAnnotationScanner() {
        onlyConsiderResourcePackages = true;
    }

    @Override
    public Set<Class<?>> classes() {
        Set<Class<?>> classes = super.classes();
        Set<Class<?>> output = new HashSet<>();
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
