package io.swagger.jaxrs2.integration;

import io.swagger.oas.integration.OpenApiConfiguration;
import io.swagger.oas.integration.OpenApiScanner;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationJaxrsScanner<T extends AnnotationJaxrsScanner<T>> implements OpenApiScanner {

    private OpenApiConfiguration openApiConfiguration;
    protected static Logger LOGGER = LoggerFactory.getLogger(AnnotationJaxrsScanner.class);

    public T withOpenApiConfiguration (OpenApiConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
        return (T)this;
    }

    @Override
    public Set<Class<?>> classes() {

        LOGGER.trace ("classes() - {}", "start");
        if (openApiConfiguration == null) {
            LOGGER.trace ("classes() - {}", "config null");
            openApiConfiguration = new OpenApiConfiguration();
        }

        ConfigurationBuilder config = new ConfigurationBuilder();
        Set<String> acceptablePackages = new HashSet<String>();

        boolean allowAllPackages = false;
        if (StringUtils.isNotBlank(openApiConfiguration.getResourcePackageNames())) {
            String[] parts = openApiConfiguration.getResourcePackageNames().split(",");
            for (String pkg : parts) {
                if (!"".equals(pkg)) {
                    acceptablePackages.add(pkg);
                    config.addUrls(ClasspathHelper.forPackage(pkg));
                }
            }
        } else {
            allowAllPackages = true;
        }
        config.setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
        final Reflections reflections;
        reflections = new Reflections(config);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(javax.ws.rs.Path.class);
        // TODO add if adding annotations
        //classes.addAll(reflections.getTypesAnnotatedWith(OpenApiDefinition.class));

        Set<Class<?>> output = new HashSet<Class<?>>();
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
        LOGGER.trace ("classes() - output size {}", output.size());
        return output;
    }

    @Override
    public Map<String, Object> resources() {
        return new HashMap<String, Object>();
    }
}
