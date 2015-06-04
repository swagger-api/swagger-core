package io.swagger.sample;

import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {
  public Application() {
    packages("io.swagger.sample.resource").
    packages("io.swagger.sample.util").
    register(io.swagger.jaxrs.listing.ApiListingResource.class).
    register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
  }
}
