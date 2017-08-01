package io.swagger.oas.integration;

import io.swagger.oas.web.OpenAPIConfig;
import io.swagger.oas.web.OpenApiScanner;
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

public class GenericOpenApiScanner implements OpenApiScanner {

    private static Logger LOGGER = LoggerFactory.getLogger(GenericOpenApiScanner.class);

    OpenApiConfiguration openApiConfiguration;

    @Override
    public void setConfiguration(OpenAPIConfig openApiConfiguration) {
        this.openApiConfiguration = ContextUtils.cloneConfigFromInterface(openApiConfiguration);
    }

    @Override
    public Set<Class<?>> classes() {
        ConfigurationBuilder config = new ConfigurationBuilder();
        Set<String> acceptablePackages = new HashSet<String>();
        Set<String> resourceClasses = new HashSet<String>();

        Set<Class<?>> output = new HashSet<Class<?>>();

        boolean allowAllPackages = false;

        // if classes are passed, use them
        if (openApiConfiguration.getResourceClasses() != null && !openApiConfiguration.getResourceClasses().isEmpty()) {
            for (String className : openApiConfiguration.getResourceClasses()) {
                if (!"".equals(className)) {
                    try {
                        output.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        LOGGER.warn("error loading class from resourceClasses: " + e.getMessage(), e);
                    }
                }
            }
            return output;
        }


        if (openApiConfiguration.getResourcePackages() != null && !openApiConfiguration.getResourcePackages().isEmpty()) {
            for (String pkg : openApiConfiguration.getResourcePackages()) {
                if (!"".equals(pkg)) {
                    acceptablePackages.add(pkg);
                    config.addUrls(ClasspathHelper.forPackage(pkg));
                }
            }
        } else {
            allowAllPackages = true;
        }

        config.setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());

        //final Reflections reflections = new Reflections(config);
        // TODO if we add an @Api annotation scan for that, same for defintion
        // this is generic, specific Jaxrs scanner will also look for @Path
/*
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Api.class);
        classes.addAll(reflections.getTypesAnnotatedWith(OpenApiDefinition.class));

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
*/
        return output;
    }

    @Override
    public Map<String, Object> resources() {
        return new HashMap<>();
    }
}
