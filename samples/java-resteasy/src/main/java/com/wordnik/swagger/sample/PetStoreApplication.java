package com.wordnik.swagger.sample;

import com.wordnik.swagger.sample.resource.PetResource;
import com.wordnik.swagger.sample.resource.PetStoreResource;
import com.wordnik.swagger.sample.resource.UserResource;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class PetStoreApplication extends Application {
    HashSet<Object> singletons = new HashSet<Object>();

    public PetStoreApplication() {
    }

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> set = new HashSet<Class<?>>();

        set.add(PetResource.class);
        set.add(UserResource.class);
        set.add(PetStoreResource.class);

        set.add(com.wordnik.swagger.jaxrs.listing.ApiListingResource.class);
        set.add(com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider.class);
        set.add(com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON.class);
        set.add(com.wordnik.swagger.jaxrs.listing.ResourceListingProvider.class);

        return set;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}


