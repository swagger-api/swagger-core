package com.wordnik.swagger.sample;

import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {
  public Application() {
    packages("com.wordnik.swagger.sample.resource").
    packages("com.wordnik.swagger.sample.util").
    register(com.wordnik.swagger.jaxrs.listing.ApiListingResource.class).
    register(com.wordnik.swagger.jaxrs.listing.SwaggerSerializers.class);
  }
}
