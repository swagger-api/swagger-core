package io.swagger.servlet.extensions;

import io.swagger.models.Operation;
import io.swagger.models.SecurityRequirement;
import io.swagger.servlet.ReaderContext;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;

public class SecurityRequirementsTest extends BaseServletReaderExtensionTest {

    @DataProvider
    private Object[][] resourceWithAnnotations() {
        return new Object[][]{
                {"testMethod1", new SecurityRequirement().requirement("api_auth", Collections.singletonList("api_auth_scope"))},
                {"testMethod2", new SecurityRequirement().requirement("api_auth", Collections.singletonList("api_auth_scope"))},
                {"testMethod3", new SecurityRequirement()
                        .requirement("operation_auth", Collections.singletonList("operation_auth_scope"))},
                {"testMethod4", new SecurityRequirement().requirement("api_auth", Collections.singletonList("api_auth_scope"))}
        };
    }

    @DataProvider
    private Object[][] resourceWithoutApiAnnotation() {
        return new Object[][]{
                {"testMethod1", null},
                {"testMethod2", null},
                {"testMethod3", new SecurityRequirement().requirement("operation_auth")},
                {"testMethod4", null}
        };
    }

    @Test(dataProvider = "resourceWithAnnotations")
    public void securityRequirementsTest1(String methodName,
            SecurityRequirement expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContext();
        extension.applySecurityRequirements(context, operation, findMethod(context, methodName));

        Assert.assertEquals(operation.getSecurity().get(0), expected.getRequirements());
    }

    @Test(dataProvider = "resourceWithoutApiAnnotation")
    public void securityRequirementsTest2(String methodName,
            SecurityRequirement expected) throws NoSuchMethodException {
        final Operation operation = new Operation();
        final ReaderContext context = createDefaultContextWithoutApi();
        extension.applySecurityRequirements(context, operation, findMethod(context, methodName));

        if (expected == null) {
            Assert.assertNull(operation.getSecurity());
        } else {
            Assert.assertEquals(operation.getSecurity().get(0), expected.getRequirements());
        }
    }
}
