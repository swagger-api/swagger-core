package io.swagger.util;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.swagger.models.properties.PropertyBuilder;
import io.swagger.util.supplier.generator.bean.MyEnum;

/**
 * @author dedece35
 */
public class AllowableValuesUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateFromEnumClass_NominalCase() {

        // call method to test
        AllowableValues av = AllowableValuesUtils.createFromEnumClass(MyEnum.class.getName());

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
        Assert.assertEquals(lst.size(), 4); // MyEnum.values().length
        Assert.assertEquals(lst.get(0), MyEnum.VALUE_1.name());
        Assert.assertEquals(lst.get(1), MyEnum.VALUE_2.name());
        Assert.assertEquals(lst.get(2), MyEnum.VALUE_3.name());
        Assert.assertEquals(lst.get(3), MyEnum.VALUE_4.name());

    }

    @Test
    public void testCreateFromEnumClass_EmptyNameCase() {
        // call method to test
        AllowableValues av = AllowableValuesUtils.createFromEnumClass("");
        //checks
        Assert.assertNull(av);
    }

    @Test
    public void testCreateFromEnumClass_NullNameCase() {
        // call method to test
        AllowableValues av = AllowableValuesUtils.createFromEnumClass(null);
        //checks
        Assert.assertNull(av);
    }

    @Test
    public void testCreateFromEnumClass_UnknownEnumCase() {
        // call method to test
        AllowableValues av = AllowableValuesUtils.createFromEnumClass("fr.mypackage.unknown.MyEnum");
        //checks
        Assert.assertNull(av);
    }

}
