package io.swagger.jaxrs.listing;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.swagger.functional.test.resources.CarResource;
import io.swagger.models.Swagger;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


public class BaseApiListingResourceTest {

    @Test(description = "it should process everything concurrently")
    public void testProcessConcurrent() throws InterruptedException, ExecutionException  {
        //given
        final Application app = mock(Application.class);
        final MockApiListingResource underTest = new MockApiListingResource();
        final ServletContext servletContext = mock(ServletContext.class);
        final ServletConfig servletConfig = mock(ServletConfig.class);
        final HttpHeaders headers = mock(HttpHeaders.class);
        final UriInfo uriInfo = mock(UriInfo.class);
        final URI uri = URI.create("testUrl");

        given(app.getClasses()).willReturn(new HashSet<Class<?>>(Arrays.asList(CarResource.class)));
        given(servletConfig.getServletContext()).willReturn(servletContext);
        given(uriInfo.getBaseUri()).willReturn(uri);

        //when
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<Swagger>> futures = executorService.invokeAll(Arrays.asList(
                callProcess(underTest, app, servletContext, servletConfig, headers, uriInfo),
                callProcess(underTest, app, servletContext, servletConfig, headers, uriInfo)
        ));
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        //then
        Swagger swaggerA = futures.get(0).get();
        Swagger swaggerB = futures.get(1).get();

        Assert.assertNotNull(swaggerA);
        Assert.assertNotNull(swaggerB);
        Assert.assertEquals(swaggerA, swaggerB);
    }

    private Callable<Swagger> callProcess(final MockApiListingResource underTest, final Application app, final ServletContext servletContext, final ServletConfig servletConfig, final HttpHeaders headers, final UriInfo uriInfo) {
        return new Callable<Swagger>() {
            @Override
            public Swagger call() {
                return underTest.process(app, servletContext, servletConfig, headers, uriInfo);
            }
        };
    }

    private class MockApiListingResource extends BaseApiListingResource {

    }
}