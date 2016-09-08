package io.swagger;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import io.swagger.models.ModelImpl;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.util.ParameterProcessor;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class ParameterProcessorTest {

    private void parametrizedMethod(
            @ApiParam(name = "paramName1", value = "paramValue1", defaultValue = "value1", required = true,
                    allowableValues = "one, two, three, ")
            @PathParam("param1") String arg1,
            @ApiParam(name = "paramName2", access = "test")
            @DefaultValue("10")
            @QueryParam("param2") List<Integer> arg2,
            @Context String arg3,
            @ApiParam(name = "paramName4", hidden = true)
            @QueryParam("hiddenParam") String arg4,
            @ApiParam(name = "paramName4")
            Integer arg5) {
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramName1", value = "paramValue1", dataType = "string", paramType = "path",
                    allowMultiple = true, allowableValues = ",,,"),
            @ApiImplicitParam(value = "paramValue2", dataType = "string", paramType = "body", access = "test",
                    defaultValue = "10")
    })
    private void implicitParametrizedMethod() {

    }

    private void rangedParametrizedMethod(
            @ApiParam(value = "sample param data", defaultValue = "5", allowableValues = "range[0,10]")
            @PathParam("id") Integer id,
            @ApiParam(value = "sample positive infinity data", allowableValues = "range(0, infinity)")
            @PathParam("minValue") Double minValue,
            @ApiParam(value = "sample negative infinity data", allowableValues = "range[-infinity, 100]")
            @PathParam("maxValue") Integer maxValue,
            @ApiParam(value = "sample array data", allowMultiple = true, allowableValues = "range(0, 5)")
            @PathParam("values") Integer values) {

    }

    private void arrayParametrizedMethod(@HeaderParam("ids") @Size(min = 5, max = 10) List<Long> ids) {

    }

    @Test(description = "parse parameters from method")
    public void parameterProcessorTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("parametrizedMethod", String.class, List.class,
                String.class, String.class, Integer.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final PathParameter p1 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));

        Assert.assertNotNull(p1);
        assertEquals(p1.getIn(), "path");
        assertEquals(p1.getName(), "paramName1");
        assertEquals(p1.getDescription(), "paramValue1");
        assertEquals(p1.getDefaultValue(), "value1");
        Assert.assertTrue(p1.getRequired());
        assertEquals(p1.getEnum(), Arrays.asList("one", "two", "three"));
        Assert.assertNull(p1.getAccess());

        final QueryParameter p2 = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter()
                .items(new IntegerProperty()), genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        Assert.assertNotNull(p2);

        final IntegerProperty items = (IntegerProperty) p2.getItems();

        Assert.assertNotNull(items);
        assertEquals(p2.getIn(), "query");
        assertEquals(p2.getName(), "paramName2");
        Assert.assertNull(p2.getDescription());
        assertEquals((int) items.getDefault(), 10);
        Assert.assertFalse(p2.getRequired());
        assertEquals(p2.getAccess(), "test");

        final Parameter p3 = ParameterProcessor.applyAnnotations(null, null,
                genericParameterTypes[2], Arrays.asList(paramAnnotations[2]));
        Assert.assertNull(p3);

        final Parameter p4 = ParameterProcessor.applyAnnotations(null, null,
                genericParameterTypes[3], Arrays.asList(paramAnnotations[3]));
        Assert.assertNull(p4);

        final BodyParameter p5 = (BodyParameter) ParameterProcessor.applyAnnotations(null, null,
                genericParameterTypes[4], Arrays.asList(paramAnnotations[4]));
        Assert.assertNotNull(p5);
        assertEquals(p5.getIn(), "body");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "arrayParam", value = "paramValue1", dataType = "string", paramType = "path",
                    allowMultiple = true)
    })
    private void implicitArrayParametrizedMethod() {}

    @Test(description = "parse implicit parameters from method")
    public void implicitArrayParameterProcessorTest() throws NoSuchMethodException {
        final ApiImplicitParams params = getClass().getDeclaredMethod("implicitArrayParametrizedMethod")
                .getAnnotation(ApiImplicitParams.class);

        final PathParameter param0 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter(),
                String.class, Collections.<Annotation>singletonList(params.value()[0]));

        assertEquals(param0.getType(), "array");
        assertEquals(param0.getItems().getType(), "string");
    }

        @Test(description = "parse implicit parameters from method")
    public void implicitParameterProcessorTest() throws NoSuchMethodException {
        final ApiImplicitParams params = getClass().getDeclaredMethod("implicitParametrizedMethod")
                .getAnnotation(ApiImplicitParams.class);
        final PathParameter param0 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter(),
                String.class, Collections.<Annotation>singletonList(params.value()[0]));

        Assert.assertNotNull(param0);
        assertEquals(param0.getIn(), "path");
        assertEquals(param0.getName(), "paramName1");
        assertEquals(param0.getDescription(), "paramValue1");
        Assert.assertNull(param0.getEnum());
        Assert.assertNotNull(param0.getItems());

        final BodyParameter param1 = (BodyParameter) ParameterProcessor.applyAnnotations(null, new BodyParameter(),
                String.class, Collections.<Annotation>singletonList(params.value()[1]));
        Assert.assertNotNull(param1);
        assertEquals(param1.getIn(), "body");
        assertEquals(param1.getName(), "body");
        assertEquals(param1.getDescription(), "paramValue2");
        assertEquals(param1.getAccess(), "test");

        final ModelImpl model = (ModelImpl) param1.getSchema();
        Assert.assertNotNull(model);
        assertEquals(model.getDefaultValue(), "10");
    }

    @Test
    public void resourceWithParamRangeTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("rangedParametrizedMethod", Integer.class, Double.class,
                Integer.class, Integer.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final PathParameter param0 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        Assert.assertNotNull(param0);
        assertEquals(param0.getDefaultValue(), "5");
        assertEquals(param0.getMinimum(), 0.0);
        assertEquals(param0.getMaximum(), 10.0);

        final PathParameter param1 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter(),
                genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        Assert.assertNotNull(param1);
        assertEquals(param1.getMinimum(), 0.0);
        Assert.assertNull(param1.getMaximum(), null);
        Assert.assertTrue(param1.isExclusiveMinimum());
        Assert.assertTrue(param1.isExclusiveMaximum());

        final PathParameter param2 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter(),
                genericParameterTypes[2], Arrays.asList(paramAnnotations[2]));
        Assert.assertNotNull(param2);
        Assert.assertNull(param2.getMinimum());
        assertEquals(param2.getMaximum(), 100.0);

        final PathParameter param3 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter()
                .items(new IntegerProperty()), genericParameterTypes[3], Arrays.asList(paramAnnotations[3]));
        Assert.assertNotNull(param3);
        final IntegerProperty items = (IntegerProperty) param3.getItems();
        Assert.assertNotNull(items);
        assertEquals(items.getMinimum(), 0.0);
        assertEquals(items.getMaximum(), 5.0);
        Assert.assertTrue(items.getExclusiveMinimum());
        Assert.assertTrue(items.getExclusiveMaximum());
    }

    @Test
    public void resourceWithArrayParamTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("arrayParametrizedMethod", List.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final HeaderParameter param = (HeaderParameter) ParameterProcessor.applyAnnotations(null, new HeaderParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        Assert.assertNotNull(param);
        assertEquals((int) param.getMinItems(), 5);
        assertEquals((int) param.getMaxItems(), 10);
    }
}
