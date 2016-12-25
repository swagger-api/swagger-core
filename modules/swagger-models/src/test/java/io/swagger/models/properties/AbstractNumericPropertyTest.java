package io.swagger.models.properties;

import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;


public class AbstractNumericPropertyTest {

    /*
     * Tests getters and setters methods on {@link AbstractNumericProperty} It was not
     * possible to cove it with {@link io.swagger.PojosTest} so a manual
     * implementation is provided for now TODO improve PojosTest to test getters
     * and setters for abstracts classes
     */
    @Test
    public void testGettersAndSetters() {
        //given
        Double minimum = 2.2, maximum = 6.4, multipleOf = 1.1;
        Boolean exclusiveMinimum = true, exclusiveMaximum = true;
        AbstractNumericProperty abstractNumericProperty = new BaseIntegerProperty();

        //when
        abstractNumericProperty.setMinimum(new BigDecimal(minimum));

        //then
        assertEquals(abstractNumericProperty.getMinimum(), new BigDecimal(minimum), "The get minimum must return the same as the set one");

        //when
        abstractNumericProperty.setMaximum(new BigDecimal(maximum));

        //then
        assertEquals(abstractNumericProperty.getMaximum(), new BigDecimal(maximum), "The get maximum must return the same as the set one");

        //when
        abstractNumericProperty.setExclusiveMaximum(exclusiveMaximum);

        //then
        assertEquals(abstractNumericProperty.getExclusiveMaximum(), exclusiveMaximum, "The get exclusiveMaximum must return the same as the set one");

        //when
        abstractNumericProperty.setExclusiveMinimum(exclusiveMinimum);

        //then
        assertEquals(abstractNumericProperty.getExclusiveMinimum(), exclusiveMinimum, "The get exclusiveMinimum must return the same as the set one");

        //when
        abstractNumericProperty.setMultipleOf(new BigDecimal(multipleOf));

        //then
        assertEquals(abstractNumericProperty.getMultipleOf(), new BigDecimal(multipleOf), "The get multipleOf must return the same as the set one");
    }
}
