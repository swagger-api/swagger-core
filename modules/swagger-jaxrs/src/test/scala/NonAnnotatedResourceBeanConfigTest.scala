import testresources._
import com.wordnik.swagger.jaxrs.reader._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import com.wordnik.swagger.config._
import com.wordnik.swagger.core.filter._
import java.lang.reflect.Method
import java.util.Date
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import scala.collection.mutable.ListBuffer
import com.wordnik.swagger.jaxrs.config.BeanConfig
import com.wordnik.swagger.reader.ClassReaders
import com.wordnik.swagger.config.ConfigFactory

@RunWith(classOf[JUnitRunner])
class NonAnnotatedResourceBeanConfigTest extends FlatSpec with Matchers {
  it should "read a resource without Api annotations" in {
    val beanConfig = new BeanConfig()
    beanConfig.setApiReader("com.wordnik.swagger.jaxrs.reader.BasicJaxrsReader")
    beanConfig.setResourcePackage("testresources")
    
    val reader =  ClassReaders.reader.get
    val config = ConfigFactory.config
    
    val classes = beanConfig.classesFromContext(null, null)
    classes should contain (classOf[NonAnnotatedResource])
    
    val apiResource = reader.read("/api-docs", classOf[NonAnnotatedResource], config).getOrElse(fail("should not be None"))
    apiResource.apis should have size 2
    
    val apis = apiResource.apis.map(m => (m.path -> m)).toMap    

    val api1 = apis("/basic/{id}/r")
    val ops1 = api1.operations.map(m => (m.method -> m)).toMap
    val getOp1 = ops1("GET")
    getOp1.responseClass should be ("void")

    val api2 = apis("/basic/{id}")
    val ops2 = api2.operations.map(m => (m.method -> m)).toMap

    val getOp2 = ops2("GET")
    getOp2.responseClass should be ("Howdy")

    val putOp2 = ops2("PUT")
    putOp2.responseClass should be ("void")

    val models = apiResource.models.get
    val howdy = models("Howdy")    
  }
  
  it should "not read a resource without Api annotations" in {
    val beanConfig = new BeanConfig()
    beanConfig.setResourcePackage("testresources")
    
    val reader =  ClassReaders.reader.get
    val config = ConfigFactory.config
    
    val classes = beanConfig.classesFromContext(null, null)
    classes should contain (classOf[NonAnnotatedResource])
    
    val apiResource = reader.read("/api-docs", classOf[NonAnnotatedResource], config)
    apiResource should be === None 
  }
}
