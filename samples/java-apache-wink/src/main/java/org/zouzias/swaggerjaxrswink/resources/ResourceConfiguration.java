package org.zouzias.swaggerjaxrswink.resources;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class ResourceConfiguration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        classes.add(com.wordnik.swagger.jaxrs.listing.ResourceListingProvider.class);
        classes.add(com.wordnik.swagger.jaxrs.listing.ApiListingResource.class);
        classes.add(com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider.class);
        classes.add(com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON.class);
        classes.add(com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider.class);

        addRestResources(classes);

        return classes;
    }

    /**
     * Here you can add your extra resources.
     *
     * @param resources
     */
    private void addRestResources(Set<Class<?>> resources) {
        resources.add(HelloResource.class);
        resources.add(HelloWorldResource.class);
    }
}
