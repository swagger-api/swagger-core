package io.swagger;

import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.util.ParameterProcessor;
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class ParameterProcessorTest {

    private static final String TEST_PATTERN_REGXP = "^[A-Z]+$";

    private void parametrizedMethod(
            @io.swagger.oas.annotations.Parameter(
                description = "paramValue1",
                required = true,
                in = "path",
                schema = @io.swagger.oas.annotations.media.Schema(
                    _default = "value1",
                    _enum = {"one", "two", "three"}))
            @PathParam("param1") String arg1,

            @io.swagger.oas.annotations.Parameter(
                    name = "param2",
                    in = "query")
            @DefaultValue("10")
            List<Integer> arg2,

            @Context String arg3,
            @io.swagger.oas.annotations.Parameter(
                    name = "hiddenParam", hidden = true)
            String arg4,
            @io.swagger.oas.annotations.Parameter(
                name = "paramName4")
            Integer arg5) {
    }

    @io.swagger.oas.annotations.Parameter(name = "paramName1", description = "paramValue1", in = "path",
            array = @io.swagger.oas.annotations.media.ArraySchema(uniqueItems = true, schema = @io.swagger.oas.annotations.media.Schema(type = "string"))
    )
    private void implicitParametrizedMethod() {

    }

    private void rangedParametrizedMethod(
            @io.swagger.oas.annotations.Parameter(description = "sample param data", style = "simple",
                    schema = @io.swagger.oas.annotations.media.Schema( _default = "5", minimum = "0", maximum = "10"))
            @PathParam("id") Integer id,

            @io.swagger.oas.annotations.Parameter(description = "sample positive infinity data",
                    array = @io.swagger.oas.annotations.media.ArraySchema(schema = @io.swagger.oas.annotations.media.Schema(minimum = "0", exclusiveMinimum = true, exclusiveMaximum = true)))
            @PathParam("minValue") Double minValue,

            @io.swagger.oas.annotations.Parameter(description = "sample negative infinity data",
                    schema = @io.swagger.oas.annotations.media.Schema(maximum = "100"))
            @PathParam("maxValue") Integer maxValue,

            @io.swagger.oas.annotations.Parameter(description = "sample array data",
                    array = @io.swagger.oas.annotations.media.ArraySchema(schema = @io.swagger.oas.annotations.media.Schema(minimum = "0", maximum = "5", exclusiveMinimum = true, exclusiveMaximum = true)))
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

            @io.swagger.oas.annotations.media.ArraySchema
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

        final Parameter p1 = (Parameter) ParameterProcessor.applyAnnotations(null, new Parameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));

        assertNotNull(p1);
        assertEquals(p1.getIn(), "path");
        assertEquals(p1.getName(), "param1");
        assertEquals(p1.getDescription(), "paramValue1");

        io.swagger.oas.models.media.StringSchema p1Schema = (StringSchema)p1.getSchema();

        assertNotNull(p1Schema);
        assertEquals(p1Schema.getDefault(), "value1");

        assertTrue(p1.getRequired());
        assertEquals(p1Schema.getEnum(), Arrays.asList("one", "two", "three"));

        final Parameter p2 = (Parameter) ParameterProcessor.applyAnnotations(null, new Parameter()
                .schema(new io.swagger.oas.models.media.Schema()), genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        assertNotNull(p2);

        final io.swagger.oas.models.media.ArraySchema items = (io.swagger.oas.models.media.ArraySchema) p2.getSchema();

        assertNotNull(items);
        assertEquals(p2.getIn(), "query");
        assertEquals(p2.getName(), "param2");
        assertNull(p2.getDescription());
        assertEquals((int) items.getItems().getDefault(), 10);
        assertNull(p2.getRequired());

        final Parameter p3 = ParameterProcessor.applyAnnotations(null, null,
                genericParameterTypes[2], Arrays.asList(paramAnnotations[2]));
        assertNull(p3);

        final Parameter p4 = ParameterProcessor.applyAnnotations(null, null,
                genericParameterTypes[3], Arrays.asList(paramAnnotations[3]));
        assertNull(p4);

//        final BodyParameter p5 = (BodyParameter) ParameterProcessor.applyAnnotations(null, null,
//                genericParameterTypes[4], Arrays.asList(paramAnnotations[4]));
//        assertNotNull(p5);
//        assertEquals(p5.getIn(), "body");
    }

    @io.swagger.oas.annotations.Parameter(name = "arrayParam", description = "paramValue1", in = "path",
            array = @io.swagger.oas.annotations.media.ArraySchema(
                    schema = @io.swagger.oas.annotations.media.Schema(type = "string"))
    )
    private void implicitArrayParametrizedMethod() {
    }

    @Test(description = "parse implicit parameters from method")
    public void implicitArrayParameterProcessorTest() throws NoSuchMethodException {
        final io.swagger.oas.annotations.Parameter params = getClass()
                .getDeclaredMethod("implicitArrayParametrizedMethod")
                .getAnnotation(io.swagger.oas.annotations.Parameter.class);

        final Parameter param0 =
                (Parameter) ParameterProcessor.applyAnnotations(
                        null,
                        new Parameter().in("path"),
                        String.class,
                        Collections.<Annotation>singletonList(params));

        assertEquals(param0.getSchema().getType(), "array");
        assertEquals(((io.swagger.oas.models.media.ArraySchema)param0.getSchema()).getItems().getType(), "string");
    }

    @Test(description = "parse implicit parameters from method")
    public void implicitParameterProcessorTest() throws NoSuchMethodException {
        final io.swagger.oas.annotations.Parameter params = getClass()
                .getDeclaredMethod("implicitParametrizedMethod")
                .getAnnotation(io.swagger.oas.annotations.Parameter.class);
        final Parameter param0 = (Parameter) ParameterProcessor.applyAnnotations(null, new Parameter().in("path"),
                String.class, Collections.<Annotation>singletonList(params));

        assertNotNull(param0);
        assertEquals(param0.getIn(), "path");
        assertEquals(param0.getName(), "paramName1");
        assertEquals(param0.getDescription(), "paramValue1");
        assertTrue(param0.getSchema() instanceof io.swagger.oas.models.media.ArraySchema);

        /*
        final BodyParameter param1 = (BodyParameter) ParameterProcessor.applyAnnotations(null, new BodyParameter(),
                String.class, Collections.<Annotation>singletonList(params.value()[1]));
        assertNotNull(param1);
        assertEquals(param1.getIn(), "body");
        assertEquals(param1.getName(), "body");
        assertEquals(param1.getDescription(), "paramValue2");
        assertEquals(param1.getAccess(), "test");

        final ModelImpl model = (ModelImpl) param1.getSchema();
        assertNotNull(model);
        assertEquals(model.getDefaultValue(), "10");*/
    }

    @Test
    public void resourceWithParamRangeTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("rangedParametrizedMethod", Integer.class, Double.class,
                Integer.class, Integer.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final Parameter param0 = (Parameter) ParameterProcessor.applyAnnotations(null, new Parameter().in("path"),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        assertNotNull(param0);
        assertEquals(param0.getSchema().getDefault(), new Integer(5));
        assertEquals(param0.getSchema().getMinimum(), new BigDecimal(0.0));
        assertEquals(param0.getSchema().getMaximum(), new BigDecimal(10.0));
//        assertEquals(param0.getCollectionFormat(), "multi");

        final Parameter param1 = (Parameter) ParameterProcessor.applyAnnotations(null, new Parameter(),
                genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        assertNotNull(param1);
        Schema param1InnerSchema = ((ArraySchema)param1.getSchema()).getItems();
        assertEquals(param1InnerSchema.getMinimum(), new BigDecimal(0.0));
        assertNull(param1InnerSchema.getMaximum(), null);
        assertTrue(param1InnerSchema.getExclusiveMinimum());
        assertTrue(param1InnerSchema.getExclusiveMaximum());

        final Parameter param2 = (Parameter) ParameterProcessor.applyAnnotations(null, new Parameter(),
                genericParameterTypes[2], Arrays.asList(paramAnnotations[2]));
        assertNotNull(param2);
        assertNull(param2.getSchema().getMinimum());
        assertEquals(param2.getSchema().getMaximum(), new BigDecimal(100.0));

        final Parameter param3 = (Parameter) ParameterProcessor.applyAnnotations(null, new Parameter()
                .schema(new io.swagger.oas.models.media.ArraySchema()
                    .items(new IntegerSchema())), genericParameterTypes[3], Arrays.asList(paramAnnotations[3]));
        assertNotNull(param3);
        final IntegerSchema items = (IntegerSchema) ((ArraySchema)param3.getSchema()).getItems();
        assertNotNull(items);
        assertEquals(items.getMinimum(), new BigDecimal(0.0));
        assertEquals(items.getMaximum(), new BigDecimal(5.0));
        assertTrue(items.getExclusiveMinimum());
        assertTrue(items.getExclusiveMaximum());
    }
/*
    @Test
    public void resourceWithArrayParamTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("arrayParametrizedMethod", List.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final HeaderParameter param = (HeaderParameter) ParameterProcessor.applyAnnotations(null, new HeaderParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        assertNotNull(param);
        assertEquals((int) param.getMinItems(), 5);
        assertEquals((int) param.getMaxItems(), 10);
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
            assertNotNull(param);
            assertEquals(param.getMinimum(), new BigDecimal(5));
            assertEquals(param.getMaximum(), new BigDecimal(10));
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
        assertNotNull(param);
        assertEquals(param.getMinLength(), new Integer(5));
        assertEquals(param.getMaxLength(), new Integer(10));
    }

    @Test
    public void beanValidationMinTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationMin",
                int.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter param = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        assertNotNull(param);
        assertEquals(param.getMinimum(), new BigDecimal(5));
    }

    @Test
    public void beanValidationMaxTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationMax",
                int.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter param = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        assertNotNull(param);
        assertEquals(param.getMaximum(), new BigDecimal(10));
    }

    @Test
    public void beanValidationDecimalMinTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationDecimalMin",
                double.class, double.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter inclusiveParam = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        assertNotNull(inclusiveParam);
        assertEquals(inclusiveParam.getMinimum(), new BigDecimal(5.5));
        assertNull(inclusiveParam.isExclusiveMinimum());

        final QueryParameter exclusiveParam = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        assertNotNull(exclusiveParam);
        assertEquals(exclusiveParam.getMinimum(), new BigDecimal(5.5));
        assertTrue(exclusiveParam.isExclusiveMinimum());
    }

    @Test
    public void beanValidationDecimalMaxTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationDecimalMax",
                double.class, double.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter inclusiveParam = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        assertNotNull(inclusiveParam);
        assertEquals(inclusiveParam.getMaximum(), new BigDecimal(10.5));
        assertNull(inclusiveParam.isExclusiveMaximum());

        final QueryParameter exclusiveParam = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        assertNotNull(exclusiveParam);
        assertEquals(exclusiveParam.getMaximum(), new BigDecimal(10.5));
        assertTrue(exclusiveParam.isExclusiveMaximum());
    }

    @Test
    public void beanValidationPatternTest() throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod("beanValidationPattern",
                String.class);
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        final QueryParameter param = (QueryParameter) ParameterProcessor.applyAnnotations(null, new QueryParameter(),
                genericParameterTypes[0], Arrays.asList(paramAnnotations[0]));
        assertNotNull(param);
        assertEquals(param.getPattern(), TEST_PATTERN_REGXP);
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
        assertNotNull(param1);
        assertEquals((int) param1.getMinItems(), 5);
        assertEquals((int) param1.getMaxItems(), 10);
        Property items1 = param1.getItems();
        assertTrue(items1 instanceof LongProperty);
        LongProperty longItems = (LongProperty) items1;
        assertEquals(longItems.getMinimum(), new BigDecimal(5));
        assertNull(longItems.getExclusiveMinimum());
        assertEquals(longItems.getMaximum(), new BigDecimal(10));
        assertNull(longItems.getExclusiveMaximum());

        //Second param - items specified
        HeaderParameter headerParam2 = new HeaderParameter().type(ArrayProperty.TYPE).items(new DoubleProperty());
        HeaderParameter param2 = (HeaderParameter) ParameterProcessor.applyAnnotations(null, headerParam2,
                genericParameterTypes[1], Arrays.asList(paramAnnotations[1]));
        assertNotNull(param2);
        assertEquals((int) param2.getMinItems(), 5);
        assertEquals((int) param2.getMaxItems(), 10);
        Property items2 = param2.getItems();
        assertTrue(items2 instanceof DoubleProperty);
        DoubleProperty doubleItems = (DoubleProperty) items2;
        assertEquals(doubleItems.getMinimum(), new BigDecimal(5.5));
        assertTrue(doubleItems.getExclusiveMinimum());
        assertEquals(doubleItems.getMaximum(), new BigDecimal(10.5));
        assertTrue(doubleItems.getExclusiveMaximum());

        //Third param - items specified
        HeaderParameter headerParam3 = new HeaderParameter().type(ArrayProperty.TYPE).items(new StringProperty());
        HeaderParameter param3 = (HeaderParameter) ParameterProcessor.applyAnnotations(null, headerParam3,
                genericParameterTypes[2], Arrays.asList(paramAnnotations[2]));
        assertNotNull(param3);
        assertEquals((int) param3.getMinItems(), 5);
        assertEquals((int) param3.getMaxItems(), 10);
        Property items3 = param3.getItems();
        assertTrue(items3 instanceof StringProperty);
        StringProperty stringItems = (StringProperty) items3;
        assertEquals(stringItems.getPattern(), TEST_PATTERN_REGXP);

        //Fourth param - items specified
        HeaderParameter headerParam4 = new HeaderParameter().type(StringProperty.TYPE);
        HeaderParameter param4 = (HeaderParameter) ParameterProcessor.applyAnnotations(null, headerParam4,
                genericParameterTypes[3], Arrays.asList(paramAnnotations[3]));
        assertNotNull(param4);
        assertEquals(param4.getType(), ArrayProperty.TYPE);
        assertEquals((int) param4.getMinItems(), 5);
        assertEquals((int) param4.getMaxItems(), 10);
        Property items4 = param4.getItems();
        assertTrue(items4 instanceof StringProperty);
    }
*/
    @io.swagger.oas.annotations.Parameter(name = "id", in = "path", required = true,
        schema = @io.swagger.oas.annotations.media.Schema(type = "integer", format = "int64"))
    private void implicitParametrizedMethodLongType() {
    }
/*
    @Test(description = "test for issue #1873 fixing.")
    public void implicitParameterLongTypeProcessorTest() throws NoSuchMethodException {
        final ApiImplicitParams params = getClass().getDeclaredMethod("implicitParametrizedMethodLongType")
                .getAnnotation(ApiImplicitParams.class);
        final PathParameter param0 = (PathParameter) ParameterProcessor.applyAnnotations(null, new PathParameter(),
                String.class, Collections.<Annotation>singletonList(params.value()[0]));

        assertEquals(param0.getName(), "id");
        assertEquals(param0.getIn(), "path");
        assertEquals(param0.getRequired(), true);
        assertEquals(param0.getType(), "integer");
        assertEquals(param0.getFormat(), "int64");

    }
    */
}
