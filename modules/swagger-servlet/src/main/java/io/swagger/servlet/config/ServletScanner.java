package io.swagger.servlet.config;

import io.swagger.annotations.Api;
import io.swagger.config.Scanner;

import org.reflections.Reflections;

import java.util.Set;

import javax.servlet.ServletConfig;

public class ServletScanner implements Scanner {

    private boolean prettyPrint = false;
    private String resourcePackage;

    public ServletScanner(ServletConfig servletConfig) {
        resourcePackage = servletConfig.getInitParameter("swagger.resource.package");
        final String shouldPrettyPrint = servletConfig.getInitParameter("swagger.pretty.print");
        if (shouldPrettyPrint != null) {
            prettyPrint = Boolean.parseBoolean(shouldPrettyPrint);
        }
    }

    public String getResourcePackage() {
        return resourcePackage;
    }

    public void setResourcePackage(String resourcePackage) {
        this.resourcePackage = resourcePackage;
    }

    @Override
    public Set<Class<?>> classes() {
        return new Reflections(resourcePackage).getTypesAnnotatedWith(Api.class);
    }

    @Override
    public boolean getPrettyPrint() {
        return prettyPrint;
    }

    @Override
    public void setPrettyPrint(boolean shouldPrettyPrint) {
        this.prettyPrint = shouldPrettyPrint;
    }
}
