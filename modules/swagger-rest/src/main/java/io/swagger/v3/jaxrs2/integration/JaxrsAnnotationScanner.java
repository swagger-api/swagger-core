package io.swagger.v3.jaxrs2.integration;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.swagger.v3.jaxrs2.integration.api.JaxrsOpenApiScanner;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Webhooks;
import io.swagger.v3.oas.integration.IgnoredPackages;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JaxrsAnnotationScanner<T extends JaxrsAnnotationScanner<T>> implements JaxrsOpenApiScanner {

    static final Set<String> ignored = new HashSet<>();

    static {
        ignored.addAll(IgnoredPackages.ignored);
    }

    protected OpenAPIConfiguration openApiConfiguration;
    protected Application application;
    protected static final Logger LOGGER = LoggerFactory.getLogger(JaxrsAnnotationScanner.class);
    protected boolean onlyConsiderResourcePackages = false;

    public JaxrsAnnotationScanner application(Application application) {
        this.application = application;
        return this;
    }

    @Override
    public void setApplication(Application application) {
        this.application = application;
    }

    public T openApiConfiguration(OpenAPIConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
        return (T) this;
    }

    @Override
    public void setConfiguration(OpenAPIConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
    }

    @Override
    public Set<Class<?>> classes() {

        if (openApiConfiguration == null) {
            openApiConfiguration = new SwaggerConfiguration();
        }

        ClassGraph graph = new ClassGraph().enableAllInfo();
        Set<String> acceptablePackages = new HashSet<>();
        Set<Class<?>> output = new HashSet<>();

        // if classes are passed, use them
        if (openApiConfiguration.getResourceClasses() != null && !openApiConfiguration.getResourceClasses().isEmpty()) {
            for (String className : openApiConfiguration.getResourceClasses()) {
                if (!isIgnored(className)) {
                    try {
                        output.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        LOGGER.warn("error loading class from resourceClasses: " + e.getMessage(), e);
                    }
                }
            }
            return output;
        }

        boolean allowAllPackages = false;
        if (openApiConfiguration.getResourcePackages() != null && !openApiConfiguration.getResourcePackages().isEmpty()) {
            for (String pkg : openApiConfiguration.getResourcePackages()) {
                if (!isIgnored(pkg)) {
                    acceptablePackages.add(pkg);
                    graph.whitelistPackages(pkg);
                }
            }
        } else {
            if (!onlyConsiderResourcePackages) {
                allowAllPackages = true;
            }
        }
        final Set<Class<?>> classes;
        try (ScanResult scanResult = graph.scan()) {
            classes = new HashSet<>(scanResult.getClassesWithAnnotation(javax.ws.rs.Path.class.getName()).loadClasses());
            classes.addAll(new HashSet<>(scanResult.getClassesWithAnnotation(OpenAPIDefinition.class.getName()).loadClasses()));
            classes.addAll(new HashSet<>(scanResult.getClassesWithAnnotation(Webhooks.class.getName()).loadClasses()));
            if (Boolean.TRUE.equals(openApiConfiguration.isAlwaysResolveAppPath())) {
                classes.addAll(new HashSet<>(scanResult.getClassesWithAnnotation(ApplicationPath.class.getName()).loadClasses()));
            }
        }

        for (Class<?> cls : classes) {
            if (allowAllPackages) {
                output.add(cls);
            } else {
                for (String pkg : acceptablePackages) {
                    if (cls.getPackage().getName().startsWith(pkg)) {
                        output.add(cls);
                    }
                }
            }
        }
        LOGGER.trace("classes() - output size {}", output.size());
        return output;
    }

    protected boolean isIgnored(String classOrPackageName) {
        if (StringUtils.isBlank(classOrPackageName)) {
            return true;
        }
        return ignored.stream().anyMatch(classOrPackageName::startsWith);
    }

    @Override
    public Map<String, Object> resources() {
        return new HashMap<>();
    }
}
