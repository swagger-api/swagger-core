package com.wordnik.swagger.jaxrs.config;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.jaxrs.Reader;
import com.wordnik.swagger.config.*;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.core.filter.*;
import com.wordnik.swagger.config.FilterFactory;

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

public class BeanConfig extends AbstractScanner implements Scanner, SwaggerConfig {
  Logger LOGGER = LoggerFactory.getLogger(BeanConfig.class);

  Reader reader = new Reader(new Swagger());

  String resourcePackage;
  String [] schemes;
  String title;
  String version;
  String description;
  String termsOfServiceUrl;
  String contact;
  String license;
  String licenseUrl;
  String filterClass;

  Info info;
  String host;
  String basePath;

  public String getResourcePackage() {
    return this.resourcePackage;
  }
  public void setResourcePackage(String resourcePackage) {
    this.resourcePackage = resourcePackage;
  }

  public String[] getSchemes() {
    return schemes;
  }
  public void setSchemes(String[] schemes) {
    this.schemes = schemes;
  }

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public String getTermsOfServiceUrl() {
    return termsOfServiceUrl;
  }
  public void setTermsOfServiceUrl(String termsOfServiceUrl) {
    this.termsOfServiceUrl = termsOfServiceUrl;
  }

  public String getContact() {
    return contact;
  }
  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getLicense() {
    return license;
  }
  public void setLicense(String license) {
    this.license = license;
  }

  public String getLicenseUrl() {
    return licenseUrl;
  }
  public void setLicenseUrl(String licenseUrl) {
    this.licenseUrl = licenseUrl;
  }

  public Info getInfo() {
    return info;
  }
  public void setInfo(Info info) {
    this.info = info;
  } 

  public String getHost() {
    return host;
  }
  public void setHost(String host) {
    this.host = host;
  }

  public String getFilterClass() {
    return filterClass;
  }
  public void setFilterClass(String filterClass) {
    this.filterClass = filterClass;
  }  

  public String getBasePath() {
    return basePath;
  }
  public void setBasePath(String basePath) {
    if(!"".equals(basePath) && basePath != null) {
      if(!basePath.startsWith("/"))
        this.basePath = "/" + basePath;
      else
        this.basePath = basePath;
    }
  }

  public void setPrettyPrint(String prettyPrint) {
    if(prettyPrint != null)
      this.prettyPrint = Boolean.parseBoolean(prettyPrint);
  }

  public void setScan(boolean shouldScan) {
    Set<Class<?>> classes = classes();
    if(classes != null)
      reader.read(classes)
        .host(host)
        .basePath(basePath)
        .info(info);
    ScannerFactory.setScanner(this);
  }

  public boolean getScan() {
    return true;
  }

  public Set<Class<?>> classes() {
    ConfigurationBuilder config = new ConfigurationBuilder();
    Set<String> acceptablePackages = new HashSet<String>();

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

    this.info = new Info()
      .description(description)
      .title(title)
      .version(version)
      .termsOfService(termsOfServiceUrl);

    if(contact != null)
      this.info.contact(new Contact()
        .name(contact));
    if(license != null && licenseUrl != null)
      this.info.license(new License()
        .name(license)
        .url(licenseUrl));
    if(schemes != null) {
      for(String scheme : schemes)
        reader.getSwagger().scheme(Scheme.forValue(scheme));
    }

    reader.getSwagger().setInfo(info);
    Set<Class<?>> classes = new Reflections(config).getTypesAnnotatedWith(Api.class);
    Set<Class<?>> output = new HashSet<Class<?>>();
    for(Class<?> cls : classes) {
      if(acceptablePackages.contains(cls.getPackage().getName()))
        output.add(cls);
    }
    return output;
  }

  public Swagger getSwagger() {
    return reader.getSwagger();
  }

  public Swagger configure(Swagger swagger) {
    if(schemes != null) {
      for(String scheme : schemes)
        swagger.scheme(Scheme.forValue(scheme));
    }
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
    return swagger.info(info)
      .host(host)
      .basePath(basePath);
  }
}
