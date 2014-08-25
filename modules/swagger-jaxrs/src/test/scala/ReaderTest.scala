import com.wordnik.swagger.jaxrs.config._

import resources.SimpleMethods

import com.wordnik.swagger.models.Swagger
import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.util.Json

import javax.ws.rs._

import java.lang.reflect.Method

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ReaderTest extends FlatSpec with Matchers {
  it should "scan methods" in {
    val methods = classOf[SimpleMethods].getMethods

    val reader = new Reader(new Swagger())
    for(method <- methods) {
      if(isValidRestPath(method)) {
        val operation = reader.parseMethod(method)
        Json.prettyPrint(operation)
      }
    }
  }

  def isValidRestPath(method: Method) = {
    if(Set(Option(method.getAnnotation(classOf[GET])),
      Option(method.getAnnotation(classOf[PUT])),
      Option(method.getAnnotation(classOf[POST])),
      Option(method.getAnnotation(classOf[DELETE])),
      Option(method.getAnnotation(classOf[OPTIONS])),
      Option(method.getAnnotation(classOf[HEAD]))).flatten.size > 0)
      true
    else
      false
  }
}
