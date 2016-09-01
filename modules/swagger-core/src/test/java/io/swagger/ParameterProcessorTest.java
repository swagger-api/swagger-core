package io.swagger;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import io.swagger.models.ModelImpl;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.ParameterProcessor;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParameterProcessorTest {

    private static final String TEST_PATTERN_REGXP = "^[A-Z]+$";

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

    private void beanValidationSizeOnNumber(
            @QueryParam("intValue") @Size(min = 5, max = 10) int intValue,
            @QueryParam("shortValue") @Size(min = 5, max = 10) short shortValue,
            @QueryParam("longValue") @Size(min = 5, max = 10) long longValue,
            @QueryParam("floatValue") @Size(min = 5, max = 10) float floatValue,
            @QueryParam("doubleValue") @Size(min = 5, max = 10) double doubleValue,
            @QueryParam("intWrapperValue") @Size(min = 5, max = 10) Integer intWrapperValue,
            @QueryParam("shortWrapperValue") @Size(min = 5, max = 10) Short shortWrapperValue,
            @QueryParam("longWrapperValue") @Size(min = 5, max = 10) Long longWrapperValue,
            @QueryParam("floatWrapperValue") @Size(min = 5, max = 10) Float floatWrapperValue,
            @QueryParam("doubleWrapperValue") @Size(min = 5, max = 10) Double doubleWrapperValue,
            @QueryParam("bigIntegerWrapperValue") @Size(min = 5, max = 10) BigInteger bigIntegerWrapperValue,
            @QueryParam("bigDecimalWrapperValue") @Size(min = 5, max = 10) BigDecimal bigDecimalWrapperValue) {
    }

    private void beanValidationSizeOnString(
            @QueryParam("stringValue") @Size(min = 5, max = 10) String stringValue) {
    }

    private void beanValidationMin(
            @QueryParam("value") @Min(value = 5) int value) {
    }

    private void beanValidationMax(
            @QueryParam("value") @Max(value = 10) int value) {
    }

    private void beanValidationDecimalMin(
            @QueryParam("inclusiveValue") @DecimalMin(value = "5.5") double inclusiveValue,
            @QueryParam("exclusiveValue") @DecimalMin(value = "5.5", inclusive = false) double exclusiveValue) {
    }

    private void beanValidationDecimalMax(
            @QueryParam("inclusiveMax") @DecimalMax(value = "10.5") double inclusiveValue,
            @QueryParam("exclusiveMax") @DecimalMax(value = "10.5", inclusive = false) double exclusiveValue) {
    }

    private void beanValidationPattern(
            @QueryParam("patternValue") @Pattern(regexp = TEST_PATTERN_REGXP) String patternValue) {
    }

    private void beanValidationArrayParametrizedMethod(
            @HeaderParam("intValues")
            @Size(min = 5, max = 10)
            @Min(value = 5)
            @Max(value = 10) List<Integer> intValues,

            @HeaderParam("doubleValues")
            @Size(min = 5, max = 10)
            @DecimalMin(value = "5.5", inclusive = false)
            @DecimalMax(value = "10.5", inclusive = false) List<Double> doubleValues,

            @HeaderParam("stringValues")
            @Size(min = 5, max = 10)
            @Pattern(regexp = TEST_PATTERN_REGXP) List<String> stringValues,

            @ApiParam(allowMultiple = true)
            @HeaderParam("allowMultipleValues")
            @Size(min = 5, max = 10) String allowMultipleValues
    ) {

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
        Assert.assertEquals(p1.getIn(), "path");
        Assert.assertEquals(p1.getName(), "paramName1");
        Assert.assertEquals(p1.getDescription(), "paramValue1");
        Assert.assertEquals(p1.getDefaultValue(), "value1");
        Assert.assertTrue(p1.getRequired());
        Assert.assertEquals(p1.getEnum(), Arrays.asList("one", "two", "three"));
        Assert.assertNull(p1.getAccess());

        final QueryParameter p2 = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter()
                .items(new IntegerProperty()), genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        Assert.assertNotNull(p2);

        final IntegerProperty items = (IntegerProperty) p2.getItems();

        Assert.assertNotNull(items);
        Assert.assertEquals(p2.getIn(), "query");
        Assert.assertEquals(p2.getName(), "paramName2");
        Assert.assertNull(p2.getDescription());
        Assert.assertEquals((int) items.getDefault(), 10);
        Assert.assertFalse(p2.getRequired());
        Assert.assertEquals(p2.getAccess(), "test");

        final Parameter p3 = ParameterProcessor.applyAnnotations(null, null,
                genericParameterTypes[2], Arrays.asList(paramAnnotations[2]));
        Assert.assertNull(p3);

        final Parameter p4 = ParameterProcessor.applyAnnotations(null, null,
                genericParameterTypes[3], Arrays.asList(paramAnnotations[3]));
        Assert.assertNull(p4);

        final BodyParameter p5 = (BodyParameter) ParameterProcessor.applyAnnotations(null, null,
                genericParameterTypes[4], Arrays.asList(paramAnnotations[4]));
        Assert.assertNotNull(p5);
        Assert.assertEquals(p5.getIn(), "body");
    }

    @Test(description = "parse implicit parameters from method")
    public void implicitParameterProcessorTest() throws NoSuchMethodException {
        final ApiImplicitParams params = getClass().getDeclaredMethod("implicitParametrizedMethod")
                .getAnnotation(ApiImplicitParams.class);
        final PathParameter param0 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter(),
                String.class, Collections.<Annotation>singletonList(params.value()[0]));

        Assert.assertNotNull(param0);
        Assert.assertEquals(param0.getIn(), "path");
        Assert.assertEquals(param0.getName(), "paramName1");
        Assert.assertEquals(param0.getDescription(), "paramValue1");
        Assert.assertNull(param0.getEnum());
        Assert.assertNotNull(param0.getItems());

        final BodyParameter param1 = (BodyParameter) ParameterProcessor.applyAnnotations(null, new BodyParameter(),
                String.class, Collections.<Annotation>singletonList(params.value()[1]));
        Assert.assertNotNull(param1);
        Assert.assertEquals(param1.getIn(), "body");
        Assert.assertEquals(param1.getName(), "body");
        Assert.assertEquals(param1.getDescription(), "paramValue2");
        Assert.assertEquals(param1.getAccess(), "test");

        final ModelImpl model = (ModelImpl) param1.getSchema();
        Assert.assertNotNull(model);
        Assert.assertEquals(model.getDefaultValue(), "10");
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
        Assert.assertEquals(param0.getDefaultValue(), "5");
        Assert.assertEquals(param0.getMinimum(), 0.0);
        Assert.assertEquals(param0.getMaximum(), 10.0);

        final PathParameter param1 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter(),
                genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        Assert.assertNotNull(param1);
        Assert.assertEquals(param1.getMinimum(), 0.0);
        Assert.assertNull(param1.getMaximum(), null);
        Assert.assertTrue(param1.isExclusiveMinimum());
        Assert.assertTrue(param1.isExclusiveMaximum());

        final PathParameter param2 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter(),
                genericParameterTypes[2], Arrays.asList(paramAnnotations[2]));
        Assert.assertNotNull(param2);
        Assert.assertNull(param2.getMinimum());
        Assert.assertEquals(param2.getMaximum(), 100.0);

        final PathParameter param3 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter()
                .items(new IntegerProperty()), genericParameterTypes[3], Arrays.asList(paramAnnotations[3]));
        Assert.assertNotNull(param3);
        final IntegerProperty items = (IntegerProperty) param3.getItems();
        Assert.assertNotNull(items);
        Assert.assertEquals(items.getMinimum(), 0.0);
        Assert.assertEquals(items.getMaximum(), 5.0);
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
        Assert.assertEquals((int) param.getMinItems(), 5);
        Assert.assertEquals((int) param.getMaxItems(), 10);
    }

    @Test
    public void beanValidationSizeOnNumberTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationSizeOnNumber",
                int.class,
                short.class,
                long.class,
                float.class,
                double.class,
                Integer.class,
                Short.class,
                Long.class,
                Float.class,
                Double.class,
                BigInteger.class,
                BigDecimal.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < 12; i++) {
            final QueryParameter param = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                    genericParameterTypes[i], Arrays.asList(paramAnnotations[i]));
            Assert.assertNotNull(param);
            Assert.assertEquals(param.getMinimum(), 5d);
            Assert.assertEquals(param.getMaximum(), 10d);
        }
    }

    @Test
    public void beanValidationSizeOnStringTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationSizeOnString",
                String.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter param = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        Assert.assertNotNull(param);
        Assert.assertEquals(param.getMinLength(), new Integer(5));
        Assert.assertEquals(param.getMaxLength(), new Integer(10));
    }

    @Test
    public void beanValidationMinTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationMin",
                int.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter param = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        Assert.assertNotNull(param);
        Assert.assertEquals(param.getMinimum(), 5d);
    }

    @Test
    public void beanValidationMaxTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationMax",
                int.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter param = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        Assert.assertNotNull(param);
        Assert.assertEquals(param.getMaximum(), 10d);
    }

    @Test
    public void beanValidationDecimalMinTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationDecimalMin",
                double.class, double.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter inclusiveParam = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        Assert.assertNotNull(inclusiveParam);
        Assert.assertEquals(inclusiveParam.getMinimum(), 5.5d);
        Assert.assertNull(inclusiveParam.isExclusiveMinimum());

        final QueryParameter exclusiveParam = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        Assert.assertNotNull(exclusiveParam);
        Assert.assertEquals(exclusiveParam.getMinimum(), 5.5d);
        Assert.assertTrue(exclusiveParam.isExclusiveMinimum());
    }

    @Test
    public void beanValidationDecimalMaxTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationDecimalMax",
                double.class, double.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter inclusiveParam = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        Assert.assertNotNull(inclusiveParam);
        Assert.assertEquals(inclusiveParam.getMaximum(), 10.5d);
        Assert.assertNull(inclusiveParam.isExclusiveMaximum());

        final QueryParameter exclusiveParam = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        Assert.assertNotNull(exclusiveParam);
        Assert.assertEquals(exclusiveParam.getMaximum(), 10.5d);
        Assert.assertTrue(exclusiveParam.isExclusiveMaximum());
    }

    @Test
    public void beanValidationPatternTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationPattern",
                String.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter param = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        Assert.assertNotNull(param);
        Assert.assertEquals(param.getPattern(), TEST_PATTERN_REGXP);
    }

    @Test
    public void beanValidationArrayParametrizedMethodTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationArrayParametrizedMethod",
                List.class, List.class, List.class, String.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        //First param - items specified
        HeaderParameter headerParam1 = new HeaderParameter().type(ArrayProperty.TYPE).items(new LongProperty());
        HeaderParameter param1 = (HeaderParameter) ParameterProcessor.applyAnnotations(null, headerParam1,
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        Assert.assertNotNull(param1);
        Assert.assertEquals((int) param1.getMinItems(), 5);
        Assert.assertEquals((int) param1.getMaxItems(), 10);
        Property items1 = param1.getItems();
        Assert.assertTrue(items1 instanceof LongProperty);
        LongProperty longItems = (LongProperty) items1;
        Assert.assertEquals(longItems.getMinimum(), 5d);
        Assert.assertNull(longItems.getExclusiveMinimum());
        Assert.assertEquals(longItems.getMaximum(), 10d);
        Assert.assertNull(longItems.getExclusiveMaximum());

        //Second param - items specified
        HeaderParameter headerParam2 = new HeaderParameter().type(ArrayProperty.TYPE).items(new DoubleProperty());
        HeaderParameter param2 = (HeaderParameter) ParameterProcessor.applyAnnotations(null, headerParam2,
                genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        Assert.assertNotNull(param2);
        Assert.assertEquals((int) param2.getMinItems(), 5);
        Assert.assertEquals((int) param2.getMaxItems(), 10);
        Property items2 = param2.getItems();
        Assert.assertTrue(items2 instanceof DoubleProperty);
        DoubleProperty doubleItems = (DoubleProperty) items2;
        Assert.assertEquals(doubleItems.getMinimum(), 5.5d);
        Assert.assertTrue(doubleItems.getExclusiveMinimum());
        Assert.assertEquals(doubleItems.getMaximum(), 10.5d);
        Assert.assertTrue(doubleItems.getExclusiveMaximum());

        //Third param - items specified
        HeaderParameter headerParam3 = new HeaderParameter().type(ArrayProperty.TYPE).items(new StringProperty());
        HeaderParameter param3 = (HeaderParameter) ParameterProcessor.applyAnnotations(null, headerParam3,
                genericParameterTypes[2], Arrays.asList(paramAnnotations[2]));
        Assert.assertNotNull(param3);
        Assert.assertEquals((int) param3.getMinItems(), 5);
        Assert.assertEquals((int) param3.getMaxItems(), 10);
        Property items3 = param3.getItems();
        Assert.assertTrue(items3 instanceof StringProperty);
        StringProperty stringItems = (StringProperty) items3;
        Assert.assertEquals(stringItems.getPattern(), TEST_PATTERN_REGXP);

        //Fourth param - items specified
        HeaderParameter headerParam4 = new HeaderParameter().type(StringProperty.TYPE);
        HeaderParameter param4 = (HeaderParameter) ParameterProcessor.applyAnnotations(null, headerParam4,
                genericParameterTypes[3], Arrays.asList(paramAnnotations[3]));
        Assert.assertNotNull(param4);
        Assert.assertEquals(param4.getType(), ArrayProperty.TYPE);
        Assert.assertEquals((int) param4.getMinItems(), 5);
        Assert.assertEquals((int) param4.getMaxItems(), 10);
        Property items4 = param4.getItems();
        Assert.assertTrue(items4 instanceof StringProperty);
    }

}
