package io.swagger.v3.core.resolving.resources;

import java.util.List;

@com.fasterxml.jackson.annotation.JsonTypeInfo(
        use = com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME,
        include = com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT)
@com.fasterxml.jackson.annotation.JsonTypeName("CustomName")
public class TestObject3699 {
    public String bar;
    public List<String> foo;
}
