package testresources

import testmodels._
import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.beans.BeanProperty

@Path("/basic")
class NonAnnotatedResource {
  @GET
  @Path("/{id}")
  def getTest(
    @QueryParam("id") id: String): Sample = {
    val out = new Sample
    out.name = "foo"
    out.value = "bar"

    out
  }

  @GET
  @Path("/{id}/r")
  def getWithResponse(
    @QueryParam("id") id: String): Response = {
    val out = new Sample
    out.name = "foo"
    out.value = "bar"
    Response.ok.entity(out).build
  }

  @PUT
  @Path("/{id}")
  def updateTest(
    sample: Sample) = {
  }
}
