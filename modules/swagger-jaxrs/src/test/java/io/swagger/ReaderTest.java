package io.swagger;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.resources.ApiConsumesProducesResource;
import io.swagger.resources.BothConsumesProducesResource;
import io.swagger.resources.DescendantResource;
import io.swagger.resources.NoConsumesProducesResource;
import io.swagger.resources.ResourceWithDeprecatedMethod;
import io.swagger.resources.ResourceWithEmptyPath;
import io.swagger.resources.ResourceWithImplicitParams;
import io.swagger.resources.ResourceWithKnownInjections;
import io.swagger.resources.RsConsumesProducesResource;
import io.swagger.resources.SimpleMethods;
import org.testng.annotations.Test;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class ReaderTest {
    private static final String APPLICATION_XML = "application/xml";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEXT_HTML = "text/html";

    @Test(description = "scan methods")
    public void scanMethods() {
        Method[] methods = SimpleMethods.class.getMethods();
        Reader reader = new Reader(new Swagger());
        for (Method method : methods) {
            if (isValidRestPath(method)) {
                Operation operation = reader.parseMethod(method);
                assertNotNull(operation);
            }
        }
    }

    @Test(description = "scan consumes and produces values with api class level annotations")
    public void scanConsumesProducesValuesWithApiClassLevelAnnotations() {
        Swagger swagger = getSwagger(ApiConsumesProducesResource.class);
        assertEquals(getGet(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_XHTML_XML);
        assertEquals(getGet(swagger, "/{id}").getProduces().get(0), MediaType.APPLICATION_ATOM_XML);
        assertEquals(getGet(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getPut(swagger, "/{id}").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getPut(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
    }

    @Test(description = "scan consumes and produces values with rs class level annotations")
    public void scanConsumesProducesValuesWithRsClassLevelAnnotations() {
        Swagger swagger = getSwagger(RsConsumesProducesResource.class);
        assertEquals(getGet(swagger, "/{id}").getConsumes().get(0), "application/yaml");
        assertEquals(getGet(swagger, "/{id}").getProduces().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getPut(swagger, "/{id}").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getPut(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
    }

    @Test(description = "scan consumes and produces values with both class level annotations")
    public void scanConsumesProducesValuesWithBothClassLevelAnnotations() {
        Swagger swagger = getSwagger(BothConsumesProducesResource.class);
        assertEquals(getGet(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_XHTML_XML);
        assertEquals(getGet(swagger, "/{id}").getProduces().get(0), MediaType.APPLICATION_ATOM_XML);
        assertEquals(getGet(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getGet(swagger, "/{id}/{name}/value").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getGet(swagger, "/{id}/{name}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getGet(swagger, "/{id}/{type}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/{type}/value").getProduces().get(0), TEXT_HTML);
        assertEquals(getPut(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getPut(swagger, "/{id}").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getPut(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
    }

    @Test(description = "scan consumes and produces values with no class level annotations")
    public void scanConsumesProducesValuesWithoutClassLevelAnnotations() {
        Swagger swagger = getSwagger(NoConsumesProducesResource.class);
        assertNull(getGet(swagger, "/{id}").getConsumes());
        assertNull(getGet(swagger, "/{id}").getProduces());
        assertEquals(getGet(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getGet(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}").getConsumes().get(0), MediaType.APPLICATION_JSON);
        assertEquals(getPut(swagger, "/{id}").getProduces().get(0), TEXT_PLAIN);
        assertEquals(getPut(swagger, "/{id}/value").getConsumes().get(0), APPLICATION_XML);
        assertEquals(getPut(swagger, "/{id}/value").getProduces().get(0), TEXT_PLAIN);
    }

    @org.junit.Test//(description = "scan class level and field level annotations")
    public void scanClassAndFieldLevelAnnotations() {
        Swagger swagger = getSwagger(ResourceWithKnownInjections.class);

        List<Parameter> resourceParameters = getGet(swagger, "/resource/{id}").getParameters();
        assertNotNull(resourceParameters);
        assertEquals(resourceParameters.size(), 3);
        assertEquals(resourceParameters.get(0).getName(), "id");
        assertEquals(resourceParameters.get(1).getName(), "fieldParam");
        assertEquals(resourceParameters.get(2).getName(), "methodParam");

        List<Parameter> subResourceParameters = getGet(swagger, "/resource/{id}/subresource1").getParameters();
        assertNotNull(subResourceParameters);
        assertEquals(subResourceParameters.size(), 3);
        assertEquals(subResourceParameters.get(0).getName(), "id");
        assertEquals(subResourceParameters.get(1).getName(), "fieldParam");
        assertEquals(subResourceParameters.get(2).getName(), "subResourceParam");
    }

    private Boolean isValidRestPath(Method method) {
        for (Class<? extends Annotation> item : Arrays.asList(GET.class, PUT.class, POST.class, DELETE.class,
                OPTIONS.class, HEAD.class)) {
            if (method.getAnnotation(item) != null) {
                return true;
            }
        }
        return false;
    }

    @Test(description = "scan overridden method in descendantResource")
    public void scanOverriddenMethod() {
        Swagger swagger = getSwagger(DescendantResource.class);
        Operation overriddenMethodWithTypedParam = getGet(swagger, "/pet/{petId1}");
        assertNotNull(overriddenMethodWithTypedParam);
        assertEquals(overriddenMethodWithTypedParam.getParameters().get(0).getDescription(), "ID of pet to return child");

        Operation methodWithoutTypedParam = getGet(swagger, "/pet/{petId2}");
        assertNotNull(methodWithoutTypedParam);

        Operation overriddenMethodWithoutTypedParam = getGet(swagger, "/pet/{petId3}");
        assertNotNull(overriddenMethodWithoutTypedParam);

        Operation methodWithoutTypedParamFromDescendant = getGet(swagger, "/pet/{petId4}");
        assertNotNull(methodWithoutTypedParamFromDescendant);

        Operation methodFromInterface = getGet(swagger, "/pet/{petId5}");
        assertNotNull(methodFromInterface);
    }

    @Test(description = "scan implicit params")
    public void scanImplicitParam() {
        Swagger swagger = getSwagger(ResourceWithImplicitParams.class);

        List<Parameter> params = swagger.getPath("/testString").getPost().getParameters();
        assertNotNull(params);
        assertEquals(params.size(), 7);
        assertEquals(params.get(0).getName(), "sort");
        assertEquals(params.get(0).getIn(), "query");

        PathParameter pathParam = (PathParameter) params.get(1);
        assertEquals(pathParam.getName(), "type");
        assertEquals(pathParam.getIn(), "path");
        assertEquals(pathParam.getEnum().size(), 3);
        assertEquals(pathParam.getType(), "string");

        HeaderParameter headerParam = (HeaderParameter) params.get(2);
        assertEquals(headerParam.getName(), "size");
        assertEquals(headerParam.getIn(), "header");
        assertEquals(headerParam.getMinimum(), 1.0);

        FormParameter formParam = (FormParameter) params.get(3);
        assertEquals(formParam.getName(), "width");
        assertEquals(formParam.getIn(), "formData");
        assertEquals(formParam.getMaximum(), 1.0);

        assertEquals(params.get(4).getName(), "width");
        assertEquals(params.get(4).getIn(), "formData");

        QueryParameter queryParam = (QueryParameter) params.get(5);
        assertEquals(queryParam.getName(), "height");
        assertEquals(queryParam.getIn(), "query");
        assertEquals(queryParam.getMinimum(), 3.0);
        assertEquals(queryParam.getMaximum(), 4.0);

        BodyParameter bodyParam = (BodyParameter) params.get(6);
        assertEquals(bodyParam.getName(), "body");
        assertEquals(bodyParam.getIn(), "body");
        assertTrue(bodyParam.getRequired());
    }

    @Test(description = "scan Deprecated annotation")
    public void scanDeprecatedAnnotation() {
        Swagger swagger = getSwagger(ResourceWithDeprecatedMethod.class);
        assertTrue(getGet(swagger, "/testDeprecated").isDeprecated());
        assertNull(getGet(swagger, "/testAllowed").isDeprecated());
    }

    @Test(description = "scan empty path annotation")
    public void scanEmptyPathAnnotation() {
        Swagger swagger = getSwagger(ResourceWithEmptyPath.class);
        assertNotNull(getGet(swagger, "/"));
    }

    private Swagger getSwagger(Class<?> cls) {
        return new Reader(new Swagger()).read(cls);
    }

    private Operation getGet(Swagger swagger, String path) {
        return swagger.getPath(path).getGet();
    }

    private Operation getPut(Swagger swagger, String path) {
        return swagger.getPath(path).getPut();
    }
}
