package io.swagger.models;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RefModelTest {

    @Test
    public void testClone() {
        // given
        RefModel refModel = new RefModel();
        refModel.asDefault("ref");
        refModel.setReference("reference");

        // when
        RefModel cloned = (RefModel) refModel.clone();

        // then
        assertEquals(cloned.getReference(), refModel.getReference(),
                "The cloned instance and the clone must have the same reference");
        assertEquals(cloned.get$ref(), refModel.get$ref(),
                "The cloned instance and the clone must have the same $ref");
        assertEquals(cloned.getSimpleRef(), refModel.getSimpleRef(),
                "The cloned instance and the clone must have the same simple reference");
        assertEquals(cloned.getVendorExtensions(), refModel.getVendorExtensions(),
                "The cloned instance and the clone must have the same vendor extensions");
    }
}
