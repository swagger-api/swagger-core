package io.swagger.v3.jaxrs2.resources;

public class Ticket2116Resource implements Ticket2116ResourceApi {

    @Override
    public String getName() {
        return "SwaggerTest";
    }

    @Override
    public Ticket2116SubResourceApi getSubResource() {
        return new Ticket2116SubResource();
    }

    @Override
    public Ticket2116SubResourceApi getAnotherSubResource() {
        return new Ticket2116SubResource();
    }

}