package com.wordnik.swagger.sample;

import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jersey.listing.JerseyApiDeclarationProvider;
import com.wordnik.swagger.jersey.listing.JerseyResourceListingProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {

  public Application() {
    packages("com.wordnik.swagger.sample.resource").
    packages("com.wordnik.swagger.sample.util").
    register(ApiListingResourceJSON.class).
    register(JerseyApiDeclarationProvider.class).
    register(JerseyResourceListingProvider.class);
  }
}
