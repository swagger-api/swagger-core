package com.wordnik.swagger.jaxrs.config;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.config.Scanner;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.config.FilterFactory;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.core.filter.*;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.HashSet;

public class ReflectiveJaxrsScanner implements SwaggerConfig, Scanner {
  Logger LOGGER = LoggerFactory.getLogger(ReflectiveJaxrsScanner.class);
  protected boolean prettyPrint = false;
  protected String resourcePackage = "";
  protected Reflections reflections;
  protected Set<String> acceptablePackages;
  protected String filterClass;

  public void setReflections(Reflections reflections) {
    this.reflections = reflections;
  }
  protected Reflections getReflections() {
    if(reflections == null) {
      ConfigurationBuilder config = new ConfigurationBuilder();
      acceptablePackages = new HashSet<String>();

      if(resourcePackage != "") {
        String[] parts = resourcePackage.split(",");
        for(String pkg : parts) {
          if(!"".equals(pkg)) {
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

  public String getResourcePackage() {
    return this.resourcePackage;
  }
  public void setResourcePackage(String resourcePackage) {
    this.resourcePackage = resourcePackage;
  }

  public Swagger configure(Swagger swagger) {
    if(filterClass != null) {
      try {
        SwaggerSpecFilter filter = (SwaggerSpecFilter) Class.forName(filterClass).newInstance();
        if(filter != null) {
          FilterFactory.setFilter(filter);
        }
      }
      catch (Exception e) {
        LOGGER.error("failed to load filter", e);
      }
    }

    return swagger;
  }

  public Set<Class<?>> classes() {
    Set<Class<?>> classes = getReflections().getTypesAnnotatedWith(Api.class);
    Set<Class<?>> output = new HashSet<Class<?>>();
    for(Class<?> cls : classes) {
      if(acceptablePackages.contains(cls.getPackage().getName()))
        output.add(cls);
    }
    return output;
  }

  public void setFilterClass(String filterClass) {
    this.filterClass = filterClass;
  }

  public String getFilterClass() {
    return filterClass;
  }
  
  public boolean getPrettyPrint() {
    return true;
  }

  public void setPrettyPrint(boolean shouldPrettyPrint) {
    prettyPrint = shouldPrettyPrint;
  }
}