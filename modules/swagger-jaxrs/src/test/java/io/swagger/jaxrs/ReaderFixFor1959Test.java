package io.swagger.jaxrs;

import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests whether the bug identified in pull request
 * <a href="https://github.com/swagger-api/swagger-core/pull/1959">1959</a> has been resolved.
 */
public class ReaderFixFor1959Test {

    @Test
    public void testParentAndChildInterface() {

        readAndCompare(new HashSet<Class<?>>(Arrays.asList(new Class<?>[]{Test1InterfaceParentResource.class, Test1InterfaceChildResource.class})),
                new HashSet<String>(Arrays.asList(new String[]{"getTestValue", "getTestValue_1"})));

    }

    @Test
    public void testClassWithImplementedInterface() {

        readAndCompare(new HashSet<Class<?>>(Arrays.asList(new Class<?>[]{Test2InterfaceChildResource.class, Test2ClassParentResource.class})),
                new HashSet<String>(Arrays.asList(new String[]{"getTestValue", "getTestValue_1"})));

    }

    @Test
    public void testParentAndChildClass() {

        readAndCompare(new HashSet<Class<?>>(Arrays.asList(new Class<?>[]{Test3ClassParentResource.class, Test3ClassChildResource.class})),
                new HashSet<String>(Arrays.asList(new String[]{"getTestValue", "getTestValue_1"})));

    }

    @Test
    public void testParentAndChildInterfaceAndImplementingParentAndChildClass() {

        readAndCompare(new HashSet<Class<?>>(Arrays.asList(new Class<?>[]{Test1InterfaceParentResource.class, Test1InterfaceChildResource.class, Test4ClassParentResource.class, Test4ClassChildResource.class})),
                new HashSet<String>(Arrays.asList(new String[]{"getTestValue", "getTestValue_1", "getTestValue_2", "getTestValue_3"})));

    }

    private void readAndCompare(Set<Class<?>> testClasses, Set<String> expectedOperationIds) {
        Swagger swagger = new Swagger();
        new Reader(swagger).read(testClasses);
        Set<String> actualOperationIds = new HashSet<String>();
        for (Path path : swagger.getPaths().values()) {
            for (Operation operation : path.getOperations()) {
                actualOperationIds.add(operation.getOperationId());
            }
        }
        Assert.assertEquals(actualOperationIds, expectedOperationIds);

    }

}
