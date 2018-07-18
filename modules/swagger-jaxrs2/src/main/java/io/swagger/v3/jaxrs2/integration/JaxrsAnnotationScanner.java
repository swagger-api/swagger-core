package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.jaxrs2.integration.api.JaxrsOpenApiScanner;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.integration.IgnoredPackages;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JaxrsAnnotationScanner<T extends JaxrsAnnotationScanner<T>> implements JaxrsOpenApiScanner {

    static final Set<String> ignored = new HashSet();

    static {
        ignored.addAll(IgnoredPackages.ignored);
    }

    protected OpenAPIConfiguration openApiConfiguration;
    protected Application application;
    protected static Logger LOGGER = LoggerFactory.getLogger(JaxrsAnnotationScanner.class);

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

        ConfigurationBuilder config = new ConfigurationBuilder();
        Set<String> acceptablePackages = new HashSet<String>();
        Set<Class<?>> output = new HashSet<Class<?>>();

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
                    config.addUrls(ClasspathHelper.forPackage(pkg));
                }
            }
        } else {
            allowAllPackages = true;
        }
        config.filterInputsBy(new FilterBuilder().exclude(".*json").exclude(".*yaml"));
        //config.filterInputsBy(new FilterBuilder().exclude(".*yaml"));
        config.setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
        final Reflections reflections;
        reflections = new Reflections(config);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(javax.ws.rs.Path.class);
        classes.addAll(reflections.getTypesAnnotatedWith(OpenAPIDefinition.class));

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
        return ignored.stream().anyMatch(i -> classOrPackageName.startsWith(i));
    }

    @Override
    public Map<String, Object> resources() {
        return new HashMap<String, Object>();
    }
}
