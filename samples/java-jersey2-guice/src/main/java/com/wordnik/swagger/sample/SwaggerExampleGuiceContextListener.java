package com.wordnik.swagger.sample;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.FilterFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.config.ReflectiveJaxrsScanner;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.model.AuthorizationType;
import com.wordnik.swagger.model.GrantType;
import com.wordnik.swagger.model.ImplicitGrant;
import com.wordnik.swagger.model.LoginEndpoint;
import com.wordnik.swagger.model.OAuthBuilder;
import com.wordnik.swagger.reader.ClassReaders;
import com.wordnik.swagger.sample.util.ApiAuthorizationFilterImpl;
import com.wordnik.swagger.sample.util.ApiOriginFilter;
import com.wordnik.swagger.sample.util.CustomFilter;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                serve("/*").with(ServletContainer.class, props);

                ReflectiveJaxrsScanner scanner = new ReflectiveJaxrsScanner();
                scanner.setResourcePackage(getClass().getPackage().getName());
                ScannerFactory.setScanner(scanner);
                SwaggerConfig config = ConfigFactory.config();
                config.setApiVersion("1.0.0");

                String basePath = "http://localhost:8002/api";
                if (System.getProperties().contains("swagger.basePath")) {
                    basePath = System.getProperty("swagger.basePath");
                }
                config.setBasePath(basePath);
                ConfigFactory.setConfig(config);

                FilterFactory.setFilter(new ApiAuthorizationFilterImpl());
                ScannerFactory.setScanner(new DefaultJaxrsScanner());
                ClassReaders.setReader(new DefaultJaxrsApiReader());

                bootstrap();

                filter("/*", ApiOriginFilter.class.getName());
            }
        });
    }

    private void bootstrap() {
        FilterFactory.setFilter(new CustomFilter());

        ApiInfo info = new ApiInfo(
                "Swagger Sample App",                             /* title */
                "This is a sample server Petstore server.  You can find out more about Swagger " +
                        "at <a href=\"http://swagger.wordnik.com\">http://swagger.wordnik.com</a> or on irc.freenode.net, #swagger.  For this sample, " +
                        "you can use the api key \"special-key\" to test the authorization filters",
                "http://helloreverb.com/terms/",                  /* TOS URL */
                "apiteam@wordnik.com",                            /* Contact */
                "Apache 2.0",                                     /* license */
                "http://www.apache.org/licenses/LICENSE-2.0.html" /* license URL */
        );

        List<String> scopes = new ArrayList<String>();
        scopes.add("PUBLIC");

        List<GrantType> grantTypes = new ArrayList<GrantType>();

        ImplicitGrant implicitGrant = new ImplicitGrant(
                new LoginEndpoint("http://localhost:8002/oauth/dialog"),
                "access_code");

        grantTypes.add(implicitGrant);

        AuthorizationType oauth = new OAuthBuilder().scopes(scopes).grantTypes(grantTypes).build();

        //  ConfigFactory.config().addAuthorization(oauth);
        ConfigFactory.config().setApiInfo(info);
    }
}
