package com.wordnik.swagger.sample.util;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.wordnik.swagger.annotations.Api;

public class BasicApplication extends Application {
  private String resourcePackage;

  @Override
  public Set<Class<?>> getClasses() {
    final ConfigurationBuilder config = new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(this.resourcePackage)).setScanners(
        new TypeAnnotationsScanner(), new SubTypesScanner());
    final Set<Class<?>> classes = new Reflections(config).getTypesAnnotatedWith(Api.class);
    return classes;
  }

  @Override
  public Set<Object> getSingletons() {
    return Collections.emptySet();
  }

  public final String getResourcePackage() {
    return this.resourcePackage;
  }

  public final void setResourcePackage(final String resourcePackage) {
    this.resourcePackage = resourcePackage;
  }
}
