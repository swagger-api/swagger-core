package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.jaxrs2.integration.resources.BaseOpenApiResource;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import org.testng.annotations.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class FailingFilterTest {

    @Path("/internal")
    public static class InternalResource {
        @GET
        public String internal() {
            return "internal";
        }
    }

    public static class VersionFilter extends AbstractSpecFilter {
        @Override
        public Optional<OpenAPI> filterOpenAPI(OpenAPI openAPI, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
            if (params.containsKey("version")) {
                Integer.parseInt(params.get("version").get(0));
            }
            return Optional.of(openAPI);
        }

        @Override
        public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
            if (api.getPath().startsWith("/internal")) {
                return Optional.empty();
            }
            return Optional.of(operation);
        }
    }

    private static ServletConfig servletConfig(String ctxId) {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameter(OpenApiContext.OPENAPI_CONTEXT_ID_KEY)).thenReturn(ctxId);
        when(config.getInitParameter(ServletConfigContextUtils.OPENAPI_CONFIGURATION_RESOURCECLASSES_KEY)).thenReturn(InternalResource.class.getName());
        when(config.getInitParameter(ServletConfigContextUtils.OPENAPI_CONFIGURATION_FILTER_KEY)).thenReturn(VersionFilter.class.getName());
        when(config.getServletName()).thenReturn(ctxId);
        when(config.getServletContext()).thenReturn(mock(ServletContext.class));
        return config;
    }

    @Test(description = "resource does not serve the unfiltered spec when the filter throws")
    public void resourceFailsClosedOnFilterError() throws Exception {
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.getCookies()).thenReturn(Collections.emptyMap());
        when(headers.getRequestHeaders()).thenReturn(new MultivaluedHashMap<>());
        UriInfo uriInfo = mock(UriInfo.class);
        MultivaluedHashMap<String, String> query = new MultivaluedHashMap<>();
        query.add("version", "x");
        when(uriInfo.getQueryParameters()).thenReturn(query);
        ServletConfig config = servletConfig("failingFilterTest.resource");

        Response response = new BaseOpenApiResource() {
            Response get() throws Exception {
                return getOpenApi(headers, config, null, uriInfo, "json");
            }
        }.get();

        assertEquals(response.getStatus(), 500);
    }

    @Test(description = "servlet does not serve the unfiltered spec when the filter throws")
    public void servletFailsClosedOnFilterError() throws Exception {
        ServletConfig config = servletConfig("failingFilterTest.servlet");
        OpenApiServlet servlet = new OpenApiServlet();
        servlet.init(config);

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameterMap()).thenReturn(Collections.singletonMap("version", new String[]{"x"}));
        when(req.getHeaderNames()).thenReturn(Collections.emptyEnumeration());
        when(req.getRequestURL()).thenReturn(new StringBuffer("http://localhost/openapi.json"));
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringWriter body = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(body));

        servlet.doGet(req, resp);

        verify(resp).setStatus(500);
        verify(resp, never()).setStatus(200);
        assertEquals(body.toString(), "");
    }
}
