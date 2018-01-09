package io.swagger;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import io.swagger.jaxrs.DefaultParameterExtension;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.jersey.SwaggerJersey2Jaxrs;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.TestEnum;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.params.BaseBean;
import io.swagger.params.ChildBean;
import io.swagger.params.EnumBean;
import io.swagger.params.RefBean;
import io.swagger.resources.Resource2031;
import io.swagger.resources.ResourceWithFormData;
import io.swagger.resources.ResourceWithJacksonBean;
import io.swagger.resources.ResourceWithKnownInjections;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.testng.annotations.Test;
import org.testng.internal.collections.Pair;

import javax.ws.rs.BeanParam;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

public class SwaggerJersey2JaxrsTest {

    // Here so that we can get the params with the @BeanParam annotation instantiated properly
    void testRoute(@BeanParam BaseBean baseBean, @BeanParam ChildBean childBean, @BeanParam RefBean refBean,
                   @BeanParam EnumBean enumBean, Integer nonBean) {
    }

    void testFormDataParamRoute(@FormDataParam("file") InputStream uploadedInputStream,
                                @FormDataParam("file") FormDataContentDisposition fileDetail) {
    }

    @Test(description = "not skip all types passed to extension")
    public void testAllTypes() {
        for (Class cls : Arrays.asList(BaseBean.class, ChildBean.class, RefBean.class)) {
            Set<Type> typesToSkip = new java.util.HashSet<Type>();
            new SwaggerJersey2Jaxrs().extractParameters(new ArrayList<Annotation>(), cls, typesToSkip, SwaggerExtensions.chain());
            assertEquals(typesToSkip.size(), 0);
        }
    }

    @Test(description = "return the proper @BeanParam Parameters based on the call to extractParameters")
    public void returnProperBeanParam() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("testRoute", BaseBean.class, ChildBean.class, RefBean.class, EnumBean.class, Integer.class);
        final List<Pair<Type, Annotation[]>> parameters = getParameters(method.getGenericParameterTypes(), method.getParameterAnnotations());

        for (Pair<Type, Annotation[]> parameter : parameters) {
            Type parameterType = parameter.first();
            List<Parameter> swaggerParams = new SwaggerJersey2Jaxrs().extractParameters(Arrays.asList(parameter.second()),
                    parameterType, new HashSet<Type>(), SwaggerExtensions.chain());
            // Ensure proper number of parameters returned
            if (parameterType.equals(BaseBean.class)) {
                assertEquals(swaggerParams.size(), 2);
            } else if (parameterType.equals(ChildBean.class)) {
                assertEquals(swaggerParams.size(), 5);
            } else if (parameterType.equals(RefBean.class)) {
                assertEquals(swaggerParams.size(), 5);
            } else if (parameterType.equals(EnumBean.class)) {
                assertEquals(swaggerParams.size(), 1);
                HeaderParameter enumParam = (HeaderParameter) swaggerParams.get(0);
                assertEquals(enumParam.getType(), "string");
                final Set<String> enumValues = Sets.newHashSet(Collections2.transform(Arrays.asList(TestEnum.values()), Functions.toStringFunction()));
                assertEquals(enumParam.getEnum(), enumValues);
            } else if (parameterType.equals(Integer.class)) {
                assertEquals(swaggerParams.size(), 0);
            } else {
                fail(String.format("Parameter of type %s was not expected", parameterType));
            }

            // Ensure the proper parameter type and name is returned (The rest is handled by pre-existing logic)
            for (Parameter param : swaggerParams) {
                assertEquals(param.getName(), param.getClass().getSimpleName().replace("eter", ""));
            }
        }
    }

    @Test(description = "return the proper @BeanParam Parameters based on the call to DefaultParameterExtension.extractParameters")
    public void returnProperBeanParamWithDefaultParameterExtension() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("testRoute", BaseBean.class, ChildBean.class, RefBean.class, EnumBean.class, Integer.class);
        final List<Pair<Type, Annotation[]>> parameters = getParameters(method.getGenericParameterTypes(), method.getParameterAnnotations());

        for (Pair<Type, Annotation[]> parameter : parameters) {
            Type parameterType = parameter.first();
            List<Parameter> swaggerParams = new DefaultParameterExtension().extractParameters(Arrays.asList(parameter.second()),
                    parameterType, new HashSet<Type>(), SwaggerExtensions.chain());
            // Ensure proper number of parameters returned
            if (parameterType.equals(BaseBean.class)) {
                assertEquals(swaggerParams.size(), 2);
            } else if (parameterType.equals(ChildBean.class)) {
                assertEquals(swaggerParams.size(), 5);
            } else if (parameterType.equals(RefBean.class)) {
                assertEquals(swaggerParams.size(), 5);
            } else if (parameterType.equals(EnumBean.class)) {
                assertEquals(swaggerParams.size(), 1);
                HeaderParameter enumParam = (HeaderParameter) swaggerParams.get(0);
                assertEquals(enumParam.getType(), "string");
                final Set<String> enumValues = Sets.newHashSet(Collections2.transform(Arrays.asList(TestEnum.values()), Functions.toStringFunction()));
                assertEquals(enumParam.getEnum(), enumValues);
            } else if (parameterType.equals(Integer.class)) {
                assertEquals(swaggerParams.size(), 0);
            } else {
                fail(String.format("Parameter of type %s was not expected", parameterType));
            }

            // Ensure the proper parameter type and name is returned (The rest is handled by pre-existing logic)
            for (Parameter param : swaggerParams) {
                assertEquals(param.getName(), param.getClass().getSimpleName().replace("eter", ""));
            }
        }
    }


    @Test(description = "return the proper @FormDataParam Parameters based on the call to extractParameters")
    public void returnProperFormDataParam() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("testFormDataParamRoute", InputStream.class, FormDataContentDisposition.class);
        final List<Pair<Type, Annotation[]>> parameters = getParameters(method.getGenericParameterTypes(), method.getParameterAnnotations());

        for (Pair<Type, Annotation[]> parameter : parameters) {
            Type parameterType = parameter.first();
            List<Parameter> swaggerParams = new SwaggerJersey2Jaxrs().extractParameters(Arrays.asList(parameter.second()),
                    parameterType, new HashSet<Type>(), SwaggerExtensions.chain());
            if (parameterType.equals(InputStream.class)) {
                assertEquals(((FormParameter) swaggerParams.get(0)).getType(), "file");
            } else {
                assertEquals(swaggerParams.size(), 0);
            }
        }
    }

    private List<Pair<Type, Annotation[]>> getParameters(Type[] type, Annotation[][] annotations) {
        final Iterator<Type> typeIterator = Arrays.asList(type).iterator();
        final Iterator<Annotation[]> paramIterator = Arrays.asList(annotations).iterator();
        final List<Pair<Type, Annotation[]>> result = new ArrayList<Pair<Type, Annotation[]>>();
        while (paramIterator.hasNext() && typeIterator.hasNext()) {
            Pair<Type, Annotation[]> pair = new Pair<Type, Annotation[]>(typeIterator.next(), paramIterator.next());
            result.add(pair);
        }
        return result;
    }

    @Test(description = "scan class level and field level annotations")
    public void scanClassAnfFieldLevelAnnotations() {
        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithKnownInjections.class);
        final List<Parameter> resourceParameters = swagger.getPaths().get("/resource/{id}").getGet().getParameters();
        assertNotNull(resourceParameters);
        assertEquals(resourceParameters.size(), 4);
        assertEquals(getName(resourceParameters, 0), "fieldParam");
        assertEquals(getName(resourceParameters, 1), "skip");
        assertEquals(getName(resourceParameters, 2), "limit");
        assertEquals(getName(resourceParameters, 3), "methodParam");
    }

    @Test(description = "FormDataBodyPart should be ignored when generating the Swagger document")
    public void testFormDataBodyPart() {
        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithFormData.class);
        final List<Parameter> parameters = swagger.getPath("/test/document/{documentName}.json").getPost().getParameters();
        assertEquals(parameters.size(), 3);
        assertEquals(parameters.get(0).getName(), "documentName");
        assertEquals(parameters.get(1).getName(), "input");
        assertEquals(parameters.get(2).getName(), "id");
    }

    @Test(description = "JsonUnwrapped, JsonIgnore, JsonValue should be honoured")
    public void testJacksonFeatures() {
        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithJacksonBean.class);
        Model o = swagger.getDefinitions().get("JacksonBean");

        assertEquals(o.getProperties().keySet(), Sets.newHashSet("identity", "bean", "code", "message",
                "precodesuf", "premessagesuf"));
    }

    @Test(description = "Tests issue 2031")
    public void testIssue2031() {
        final Swagger swagger = new Reader(new Swagger()).read(Resource2031.class);
        assertNotNull(swagger);

    }

    private String getName(List<Parameter> resourceParameters, int i) {
        return resourceParameters.get(i).getName();
    }
}
