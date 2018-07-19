package io.swagger.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.swagger.models.properties.PropertyBuilder;

/**
 * @author dedece35
 */
public class AllowableEnumValuesTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCreate_NominalCase() {

        String myVal1 = "MYVAL_1";
        String myVal2 = "MYVAL_2";
        String myVal3 = "MYVAL_3";
        String myVal4 = "MYVAL_4";
        List<String> lstItems = Arrays.asList(myVal1, myVal2, myVal3, myVal4);

        // call method to test
        AllowableValues av = AllowableEnumValues.create(lstItems);

        //checks
        Assert.assertNotNull(av);
        Assert.assertNotNull(av.asPropertyArguments());
        Assert.assertEquals(av.asPropertyArguments().size(), 1);
        Assert.assertNotNull(av.asPropertyArguments().get(PropertyBuilder.PropertyId.ENUM));

        List<String> lst = null;
        try {
            lst = (List<String>) av.asPropertyArguments().get(PropertyBuilder.PropertyId.ENUM);
        } catch (ClassCastException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertNotNull(lst);
        Assert.assertEquals(lst.size(), 4);
        Assert.assertEquals(lst.get(0), myVal1);
        Assert.assertEquals(lst.get(1), myVal2);
        Assert.assertEquals(lst.get(2), myVal3);
        Assert.assertEquals(lst.get(3), myVal4);

    }

    @Test
    public void testCreate_NullItemsCase() {
        List<String> lst = null;
        // call method to test
        AllowableValues av = AllowableEnumValues.create(lst);
        //checks
        Assert.assertNull(av);
    }

    @Test
    public void testCreate_EmptyItemsCase() {
        List<String> lst = new ArrayList<>(0);
        // call method to test
        AllowableValues av = AllowableEnumValues.create(lst);
        //checks
        Assert.assertNull(av);
    }

}
