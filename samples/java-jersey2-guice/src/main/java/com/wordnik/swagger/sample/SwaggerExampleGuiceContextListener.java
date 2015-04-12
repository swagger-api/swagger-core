package com.wordnik.swagger.sample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.sample.util.ApiOriginFilter;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.HashMap;
import java.util.Map;

public class SwaggerExampleGuiceContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(ServletContainer.class).in(Singleton.class);
                bind(ApiOriginFilter.class).in(Singleton.class);

                Map<String, String> props = new HashMap<String, String>();
                props.put("javax.ws.rs.Application", Application.class.getName());
                props.put("jersey.config.server.wadl.disableWadl", "true");
                serve("/api/*").with(ServletContainer.class, props);

                serve("").with(Bootstrap.class);

                BeanConfig beanConfig = new BeanConfig();
                beanConfig.setVersion("1.0.0");
                beanConfig.setSchemes(new String[]{"http"});
                beanConfig.setHost("localhost:8002");
                beanConfig.setBasePath("/api");
                beanConfig.setFilterClass("com.wordnik.swagger.sample.util.ApiAuthorizationFilterImpl");
                beanConfig.setResourcePackage("com.wordnik.swagger.sample.resource");
                beanConfig.setScan(true);

                filter("/*").through(ApiOriginFilter.class);
            }
        });
    }
}
