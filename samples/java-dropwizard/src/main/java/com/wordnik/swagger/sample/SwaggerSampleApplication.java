package com.wordnik.swagger.sample;

import com.wordnik.swagger.sample.resource.*;

import com.wordnik.swagger.jaxrs.config.*;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.config.*;
import com.wordnik.swagger.reader.*;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class SwaggerSampleApplication extends Application <SwaggerSampleConfiguration> {
  public static void main(String[] args) throws Exception {
    new SwaggerSampleApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<SwaggerSampleConfiguration> bootstrap) { }

  @Override   
  public String getName() {
      return "swagger-sample";
  }

  @Override
  public void run(SwaggerSampleConfiguration configuration, Environment environment) {
    environment.jersey().register(new ApiListingResourceJSON());
    environment.jersey().register(new PetResource());

    environment.jersey().register(new ResourceListingProvider());
    environment.jersey().register(new ApiDeclarationProvider());
    ScannerFactory.setScanner(new DefaultJaxrsScanner());
    ClassReaders.setReader(new DefaultJaxrsApiReader());

    SwaggerConfig config = ConfigFactory.config();
    config.setApiVersion("1.0.1");
    config.setBasePath("http://localhost:8000");
  }
}