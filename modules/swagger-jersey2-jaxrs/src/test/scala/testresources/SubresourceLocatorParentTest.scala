package testresources

import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import javax.ws.rs._
import javax.ws.rs.core.{Response, MediaType}

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty
import javax.ws.rs.core.{MediaType, Response}

@Path("/parent")
@Api(value = "/parent",
  description = "Subresource Class Locator Test")
class SubresourceLocatorParentTest {
  @Path("/child")
  def subresourceLocator = classOf[SubresourceLocatorChildTest]
}