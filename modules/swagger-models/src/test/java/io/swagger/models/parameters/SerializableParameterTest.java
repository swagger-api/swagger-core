package io.swagger.models.parameters;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SerializableParameterTest {

    private static final String REQUIRED_PARAMETER_METHODS = "requiredParameterMethods";

    @DataProvider(name = REQUIRED_PARAMETER_METHODS)
    public Iterator<Object[]> createRequiredParameterMethods() {
        String[] requiredMethods = {"maximum", "exclusiveMaximum", "minimum", "exclusiveMinimum", "maxLength",
                "minLength", "pattern", "maxItems", "minItems", "uniqueItems", "multipleOf"};

        List<Object[]> resultList = new ArrayList<Object[]>(requiredMethods.length);
        for (String requiredMethod : requiredMethods) {
            resultList.add(new Object[]{requiredMethod});
        }

        return resultList.iterator();
    }

    /**
     * Tests if SerializableParameter.class has requiredParameter read method
     *
     * @param requiredParameter
     * @throws IntrospectionException
     */
    @Test(dataProvider = REQUIRED_PARAMETER_METHODS)
    public void testSerializableParameterReadMethod(String requiredParameter) {
        String errorMsg = "SerializableParameter - missing property: " + requiredParameter;
        try {
            // Gets the method that should be used to read the property value.
            Assert.assertNotNull(new PropertyDescriptor(requiredParameter, SerializableParameter.class).getReadMethod(),
                    errorMsg);
        } catch (IntrospectionException e) {
            Assert.fail(errorMsg + ", " + e.getMessage(), e);
        }
    }

    /**
     * Tests if SerializableParameter.class has requiredParameter write method
     *
     * @param requiredParameter
     * @throws IntrospectionException
     */
    @Test(dataProvider = REQUIRED_PARAMETER_METHODS)
    public void testSerializableParameterWriteMethod(String requiredParameter) {
        String errorMsg = "SerializableParameter - missing property: " + requiredParameter;
        try {
            // Gets the method that should be used to write the property value.
            Assert.assertNotNull(
                    new PropertyDescriptor(requiredParameter, SerializableParameter.class).getWriteMethod(), errorMsg);
        } catch (IntrospectionException e) {
            Assert.fail(errorMsg + ", " + e.getMessage(), e);
        }
    }

}
