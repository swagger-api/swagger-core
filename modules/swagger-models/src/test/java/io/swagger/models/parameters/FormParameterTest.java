package io.swagger.models.parameters;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class FormParameterTest {

    @Test
    public void getDefaultCollectionFormat() {
        //when
        String collectionFormat = new FormParameter().getDefaultCollectionFormat();

        //then
        assertEquals(collectionFormat, "multi", "The default format must be multi");
    }
}
