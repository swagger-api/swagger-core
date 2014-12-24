package com.wordnik.swagger.sample;

import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.ReflectiveJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import com.wordnik.swagger.sample.resource.PetResource;
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
    public void run(SwaggerSampleConfiguration configuration, Environment environment) {
        environment.addResource(new ApiListingResourceJSON());
        environment.addResource(new PetResource());

        environment.addProvider(new ResourceListingProvider());
        environment.addProvider(new ApiDeclarationProvider());

        ReflectiveJaxrsScanner scanner = new ReflectiveJaxrsScanner();
        scanner.setResourcePackage("com.wordnik");
        ScannerFactory.setScanner(scanner);

        ClassReaders.setReader(new DefaultJaxrsApiReader());

        SwaggerConfig config = ConfigFactory.config();
        config.setApiVersion("1.0.1");
        config.setBasePath("http://localhost:8000");
    }

}
