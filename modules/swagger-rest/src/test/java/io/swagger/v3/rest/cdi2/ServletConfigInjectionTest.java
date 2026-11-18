package io.swagger.v3.rest.cdi2;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.annotations.Test;

import io.swagger.v3.rest.integration.OpenApiServlet;
import io.swagger.v3.rest.integration.resources.OpenApiResource;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.servlet.ServletConfig;

import java.util.Set;

public class ServletConfigInjectionTest extends Arquillian {

    @Inject
    BeanManager beanManager;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(OpenApiServlet.class, ServletConfigProducer.class)
                .addPackage(OpenApiResource.class.getPackage())
                .addAsManifestResource("META-INF/beans.xml");
    }

    @Test
    void openApiServletIsNotDiscoveredAsBean() {
        assertTrue(beanManager.getBeans(OpenApiServlet.class).isEmpty());
    }

    // RESTEasy 7 registers a @Default ServletConfig bean and turns @Context fields into CDI injection points
    @Test
    void servletConfigResolvesToTheContainerBean() {
        Set<Bean<?>> beans = beanManager.getBeans(ServletConfig.class);
        assertEquals(beans.size(), 1);
        assertEquals(beans.iterator().next().getBeanClass(), ServletConfigProducer.class);
    }

    static class ServletConfigProducer {
        @Produces
        ServletConfig servletConfig() {
            return null;
        }
    }
}
