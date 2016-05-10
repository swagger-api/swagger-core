package io.swagger.jaxrs.config;

import io.swagger.annotations.SwaggerDefinition;
import io.swagger.config.Scanner;
import io.swagger.config.SwaggerConfig;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

/**
 * Only rely on Application subclass to find the API. It implies no scanning is involed
 * and reflections dependency is fully optional.
 */
public class ApplicationBeanConfig extends NoScanningBeanConfig implements Scanner, SwaggerConfig {
    private final Application application;

    public ApplicationBeanConfig(final Application application) {
        this.application = application;
    }

    @Override
    public Set<Class<?>> classes() {
        final DefaultJaxrsScanner scanner = new DefaultJaxrsScanner();
        scanner.setPrettyPrint(getPrettyPrint());
        final Set<Class<?>> output = scanner.classesFromContext(application, null);

        final Iterator<Class<?>> classes = output.iterator();
        while (classes.hasNext()) {
            if (classes.next().getAnnotation(Provider.class) != null) { // not an endpoint
                classes.remove();
            }
        }

        final Class<?> appClass = findApplicationClass();
        if (appClass.getAnnotation(SwaggerDefinition.class) != null) {
            output.add(appClass);
        }

        if (resourcePackage != null) {
            String[] parts = resourcePackage.split(" *, *");
            final Iterator<Class<?>> it = output.iterator();
            while (it.hasNext()) {
                boolean ignore = true;
                final Class<?> next = it.next();
                for (final String pkg : parts) {
                    if (next.getName().startsWith(pkg)) {
                        ignore = false;
                        break;
                    }
                }
                if (ignore) {
                    it.remove();
                }
            }
        }

        return output;
    }

    private Class<?> findApplicationClass() {
        Class<?> realAppClass = application.getClass();
        if ("org.apache.openejb.server.rest.InternalApplication".equals(realAppClass.getName())) { // unwrap
            try {
                final Method original = realAppClass.getMethod("getOriginal");
                final Object instance = original.invoke(application);
                if (instance != null) {
                    realAppClass = instance.getClass();
                }
            } catch (final NoSuchMethodException e) {
                // ignore
            } catch (final InvocationTargetException e) {
                // ignore
            } catch (final IllegalAccessException e) {
                // ignore
            }
        }
        return realAppClass;
    }
}
