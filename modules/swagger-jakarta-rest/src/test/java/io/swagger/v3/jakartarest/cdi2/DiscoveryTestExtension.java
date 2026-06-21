package io.swagger.v3.jakartarest.cdi2;

import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ext.Provider;

public class DiscoveryTestExtension implements Extension {

    private final Set<Class<?>> providers = new HashSet<>();
    private final Set<Class<?>> resources = new HashSet<>();

    public Set<Class<?>> getProviders() {
        return providers;
    }

    public Set<Class<?>> getResources() {
        return resources;
    }

    public <T> void observeResources(@WithAnnotations({ Path.class }) @Observes ProcessAnnotatedType<T> event) {
        AnnotatedType<T> annotatedType = event.getAnnotatedType();

        if (!annotatedType.getJavaClass().isInterface()) {
            this.resources.add(annotatedType.getJavaClass());
        }
    }

    public <T> void observeProviders(@WithAnnotations({ Provider.class }) @Observes ProcessAnnotatedType<T> event) {
        AnnotatedType<T> annotatedType = event.getAnnotatedType();

        if (!annotatedType.getJavaClass().isInterface()) {
            this.providers.add(annotatedType.getJavaClass());
        }
    }

}
