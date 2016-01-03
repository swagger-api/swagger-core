package io.swagger.models.properties;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class RefPropertyTest {
    @Test
    public void testConstructor() {
        // given
        RefProperty refProperty = new RefProperty();

        // then
        assertNull(refProperty.getRefFormat(), "The default format is null");
        assertNull(refProperty.getSimpleRef(), "The default simpleref is null");
    }

    @Test
    public void testAsDefault() {
        // given
        RefProperty refProperty = new RefProperty();

        // when
        refProperty.asDefault("ref");

        // then
        assertEquals(refProperty.getSimpleRef(), "ref", "The get simpleref must be the same as the one set as default");
    }
}
