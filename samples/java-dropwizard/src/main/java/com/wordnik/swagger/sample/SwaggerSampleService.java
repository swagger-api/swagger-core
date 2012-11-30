package com.wordnik.swagger.sample;

import com.wordnik.swagger.sample.resource.*;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class SwaggerSampleService extends Service<SwaggerSampleConfiguration> {
    public static void main(String[] args) throws Exception {
        new SwaggerSampleService().run(args);
    }

    @Override
    public void initialize(Bootstrap<SwaggerSampleConfiguration> bootstrap) {
        bootstrap.setName("swagger-sample");
    }

    @Override
    public void run(SwaggerSampleConfiguration configuration,
                    Environment environment) {
        environment.addResource(new ApiListingResourceJSON());
        environment.addResource(new PetResourceJSON());
    }
}