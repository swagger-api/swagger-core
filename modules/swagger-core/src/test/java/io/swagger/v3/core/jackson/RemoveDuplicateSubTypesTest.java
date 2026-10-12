package io.swagger.v3.core.jackson;

import org.testng.annotations.Test;
import tools.jackson.databind.jsontype.NamedType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class RemoveDuplicateSubTypesTest {

    static class A {
    }

    static class B {
    }

    @Test
    public void removesDuplicatesByClassKeepingFirstAndOrder() {
        final List<NamedType> types = new ArrayList<>(Arrays.asList(
                new NamedType(A.class),
                new NamedType(B.class),
                new NamedType(A.class),
                new NamedType(B.class, "b"),
                new NamedType(B.class, "other")));

        ModelResolver.removeDuplicateSubTypes(types);

        assertEquals(types, Arrays.asList(new NamedType(A.class), new NamedType(B.class)));
    }
}
