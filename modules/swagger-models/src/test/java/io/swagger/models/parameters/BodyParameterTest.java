package io.swagger.models.parameters;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BodyParameterTest {

    @Test
    public void testExample() {
        // given
        BodyParameter bodyParameter = new BodyParameter();
        final String mediaType = "mediaType", value = "value";

        //when
        bodyParameter.example(mediaType, value);

        //then
        assertEquals(bodyParameter.getExamples().get(mediaType), value, "The getExamples value must be the same as the set one");
    }
}
