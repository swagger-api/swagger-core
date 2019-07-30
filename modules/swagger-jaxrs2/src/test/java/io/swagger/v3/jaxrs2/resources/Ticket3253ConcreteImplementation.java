package io.swagger.v3.jaxrs2.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public class Ticket3253ConcreteImplementation extends Ticket3253Abstract<Long>
    implements Ticket3253AnnotatedInterface<Long> {
  @Override
  public Response postResource(List<Long> payload) {
      return null;
  }

  @Override
  public Response postResource(Long payload) {
      return null;
  }

  @Override
  public Response overriddenMethodWithTypedParam(Long petId) {
      return super.overriddenMethodWithTypedParam(petId);
  }

  @GET
  @Path("/{petId4}")
  public Response methodNotInherited(@PathParam("petId4") Long petId) {
      return Response.ok().build();
  }

  @Override
  public Response overriddenMethodWithoutTypedParam(Long petId) {
      return super.overriddenMethodWithoutTypedParam(petId);
  }

  @Override
  public Response methodFromInterfaceTyped(Long petId) {
      return null;
  }

  @Override
  public Response methodFromInterfaceNotTyped(Number petId) {
    return null;
  }

  @Path("{petId8}")
  @GET
  public Response methodTypedAsNumber(@PathParam("petId8") Number petId) {
    return null;
  }

  @Override
  public Response methodFromInterface(Long petId, String str) {
    return null;
  }

  @Override
  public Response deprecatedMethodFromInterface(Long petId) {
      return null;
  }

  @Override
  public String simpleMethod(String arg) {
      return null;
  }
  @Override
  public String simpleMethod2(String arg) {
      return null;
  }
}
