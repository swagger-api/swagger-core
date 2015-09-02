package io.swagger.servlet.extensions;

import io.swagger.models.Operation;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;

public class ParametersTest extends BaseServletReaderExtensionTest {

    @Test
    public void applyParametersTest() {
        final Operation operation = new Operation();
        extension.applyParameters(createDefaultContext(), operation, String.class, new Annotation[]{});

        Assert.assertEquals(operation.getParameters().size(), 0);
    }
}
