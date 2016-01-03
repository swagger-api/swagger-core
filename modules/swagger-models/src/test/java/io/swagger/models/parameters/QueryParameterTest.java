package io.swagger.models.parameters;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class QueryParameterTest {

    @Test
    public void getDefaultCollectionFormat() {
        //when
        String collectionFormat = new QueryParameter().getDefaultCollectionFormat();

        //then
        assertEquals(collectionFormat, "multi", "The default format must be 'multi'");
    }
}
