package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.servlet.ReaderContext;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class ResponsesTest extends BaseServletReaderExtensionTest {

    private static final Map<String, String> responses = new HashMap<String, String>();

    @DataProvider
    private Object[][] resourceWithAnnotations() {
        return new Object[][]{
                {"testMethod1", new Response().description("successful operation")},
                {"testMethod2", null},
                {"testMethod3", new Response().description("successful operation")},
                {"testMethod4", null}
        };
    }

    @Test(dataProvider = "resourceWithAnnotations")
    public void applyResponsesTest(String methodName, Response expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContext();
        extension.applyResponses(context, operation, findMethod(context, methodName));

        if (expected == null) {
            Assert.assertNull(operation.getResponses());
        } else {
            final Response response = operation.getResponses().get("200");
            Assert.assertEquals(response.getDescription(), expected.getDescription());
        }
    }

    @Test
    public void detailedResponsesTest() throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContext();
        extension.applyResponses(context, operation, findMethod(context, "testMethod3"));
        final Map<String, Response> responses = operation.getResponses();

        Assert.assertEquals(responses.size(), 7);
        for (Map.Entry<String, String> entry : ResponsesTest.responses.entrySet()) {
            Assert.assertEquals(responses.get(entry.getKey()).getDescription(), entry.getValue());
        }
    }

    static {
        responses.put("default", "response_annotation1");
        responses.put("200", "successful operation");
        responses.put("400", "response_annotation2");
        responses.put("401", "response_annotation3");
        responses.put("402", "response_annotation4");
        responses.put("403", "response_annotation5");
        responses.put("404", "response_annotation6");
    }
}
