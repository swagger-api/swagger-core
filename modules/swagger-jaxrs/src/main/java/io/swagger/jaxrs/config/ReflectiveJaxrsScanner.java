package io.swagger.jaxrs.config;

import io.swagger.annotations.Api;
import io.swagger.config.FilterFactory;
import io.swagger.config.Scanner;
import io.swagger.config.SwaggerConfig;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.models.Swagger;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class ReflectiveJaxrsScanner implements SwaggerConfig, Scanner {
    protected boolean prettyPrint = false;
    protected String resourcePackage = "";
    protected Reflections reflections;
    protected Set<String> acceptablePackages;
    protected String filterClass;
    Logger LOGGER = LoggerFactory.getLogger(ReflectiveJaxrsScanner.class);

    protected Reflections getReflections() {
        if (reflections == null) {
            ConfigurationBuilder config = new ConfigurationBuilder();
            acceptablePackages = new HashSet<String>();

            if (resourcePackage != "") {
                String[] parts = resourcePackage.split(",");
                for (String pkg : parts) {
                    if (!"".equals(pkg)) {
                        acceptablePackages.add(pkg);
                        config.addUrls(ClasspathHelper.forPackage(pkg));
                    }
                }
            }

            config.setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
            this.reflections = new Reflections(config);
        }
        return this.reflections;
    }

    public void setReflections(Reflections reflections) {
        this.reflections = reflections;
    }

    public String getResourcePackage() {
        return this.resourcePackage;
    }

    public void setResourcePackage(String resourcePackage) {
        this.resourcePackage = resourcePackage;
    }

    @Override
    public Swagger configure(Swagger swagger) {
        if (filterClass != null) {
            try {
                SwaggerSpecFilter filter = (SwaggerSpecFilter) Class.forName(filterClass).newInstance();
                if (filter != null) {
                    FilterFactory.setFilter(filter);
                }
            } catch (Exception e) {
                LOGGER.error("failed to load filter", e);
            }
        }

        return swagger;
    }

    @Override
    public Set<Class<?>> classes() {
        Set<Class<?>> classes = getReflections().getTypesAnnotatedWith(Api.class);
        Set<Class<?>> output = new HashSet<Class<?>>();
        for (Class<?> cls : classes) {
            if (acceptablePackages.contains(cls.getPackage().getName())) {
                output.add(cls);
            }
        }
        return output;
    }

    @Override
    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    @Override
    public boolean getPrettyPrint() {
        return true;
    }

    @Override
    public void setPrettyPrint(boolean shouldPrettyPrint) {
        prettyPrint = shouldPrettyPrint;
    }
}