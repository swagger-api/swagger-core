package io.swagger.v3.jaxrs2.resources;

import java.util.List;

import javax.ws.rs.core.Response;

public class Ticket3253ConcreteImplementation implements Ticket3253AnnotatedInterface<Long> {
    @Override
    public Response postResource(List<Long> payload) {
      return null;
    }

    @Override
    public Response postResource(Long payload) {
      return null;
    }
}