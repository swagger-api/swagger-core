package io.swagger.v3.plugin.annotator.swagger3;

import java.io.File;
import java.lang.reflect.Method;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.plugin.annotator.swagger3.resource.TestController;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwaggerAnnotatorResolveTest extends BaseSwaggerMavenIntegrationTest {

    protected static Logger LOGGER = LoggerFactory.getLogger(SwaggerAnnotatorResolveTest.class);

    public void testResolve() throws Exception {
        File pom = getTestFile("src/test/resources/pom.testSwagger3AnnotatorMojo.xml");
        //fixme
//        runTest(pom);
//        Api api = TestController.class.getDeclaredAnnotation(Api.class);
//        Assert.assertNotNull(api);
//        Method method = TestController.class.getDeclaredMethod("test", String.class);
//        Operation operation = method.getDeclaredAnnotation(Operation.class);
//        ApiResponse apiResponse = method.getDeclaredAnnotation(ApiResponse.class);
//        Assert.assertNotNull(operation);
//        Assert.assertNotNull(apiResponse);
    }

}
