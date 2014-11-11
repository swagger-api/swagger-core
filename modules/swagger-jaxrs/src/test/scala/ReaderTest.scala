import java.lang.reflect.Method
import javax.ws.rs._

import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.models.Swagger
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import resources.SimpleMethods

@RunWith(classOf[JUnitRunner])
class ReaderTest extends FlatSpec with Matchers {
  it should "scan methods" in {
    val methods = classOf[SimpleMethods].getMethods

    val reader = new Reader(new Swagger())
    for(method <- methods) {
      if(isValidRestPath(method)) {
        val operation = reader.parseMethod(method)
        operation should not be (null)
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
