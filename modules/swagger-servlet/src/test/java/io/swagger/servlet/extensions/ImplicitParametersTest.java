package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.servlet.ReaderContext;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class ImplicitParametersTest extends BaseServletReaderExtensionTest {

    @DataProvider
    private Object[][] resourceWithAnnotations() {
        return new Object[][]{
                {"testMethod1", 0},
                {"testMethod2", 0},
                {"testMethod3", 6},
                {"testMethod4", 0}
        };
    }

    @Test(dataProvider = "resourceWithAnnotations")
    public void applyImplicitParametersTest(String methodName, int expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContext();
        extension.applyImplicitParameters(context, operation, findMethod(context, methodName));

        Assert.assertEquals(operation.getParameters().size(), expected);
    }

    @Test
    public void detailedTest() throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContext();
        extension.applyImplicitParameters(context, operation, findMethod(context, "testMethod3"));

        final List<Parameter> parameters = operation.getParameters();

        final Parameter parameter1 = parameters.get(0);
        Assert.assertNotNull(parameter1);
        Assert.assertTrue(parameter1 instanceof PathParameter);
        Assert.assertEquals(parameter1.getName(), "param1");
        Assert.assertEquals(parameter1.getIn(), "path");
        Assert.assertEquals(parameter1.getDescription(), "Param 1");
        Assert.assertTrue(parameter1.getRequired());

        final Parameter parameter5 = parameters.get(4);
        Assert.assertNotNull(parameter5);
        Assert.assertTrue(parameter5 instanceof BodyParameter);
        Assert.assertEquals(parameter5.getName(), "param5");
        Assert.assertEquals(parameter5.getIn(), "body");
        Assert.assertEquals(parameter5.getDescription(), "Param 5");
        Assert.assertFalse(parameter5.getRequired());
    }
}
