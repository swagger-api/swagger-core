package com.wordnik.swagger.sample;

import com.wordnik.swagger.sample.resource.*;

import com.wordnik.swagger.jaxrs.config.*;
import com.wordnik.swagger.jaxrs.listing.ApiListingResource;
import com.wordnik.swagger.config.*;

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
    environment.jersey().register(new ApiListingResource());
    environment.jersey().register(new PetResource());

    // SwaggerConfig config = ConfigFactory.config();
    // config.setApiVersion("1.0.1");
    // config.setBasePath("http://localhost:8000");
  }
}