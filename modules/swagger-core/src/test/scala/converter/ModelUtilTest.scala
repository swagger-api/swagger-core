package converter

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ModelUtilTest extends FlatSpec with Matchers {
  import com.wordnik.swagger.core.util.ModelUtil._

  "ModelUtil cleanDataType" should "convert a fully-qualified primitive type to a SwaggerTypes primitive" in {
    val primitiveName = "java.lang.Integer"
    val cleanName = cleanDataType(primitiveName)
    cleanName should equal ("int")
  }

  it should "convert a primitive type simple name to a SwaggerTypes primitive" in {
    val primitiveName = "Integer"
    val cleanName = cleanDataType(primitiveName)
    cleanName should equal ("int")
  }

  it should "convert the inner class of a container to a SwaggerTypes primitive" in {
    val origName = "List[java.lang.Integer]"
    val cleanName = cleanDataType(origName)
    cleanName should equal ("List[int]")
  }

  it should "return a fully-qualified class name unchanged" in {
    val fqcn = "com.wordnik.swagger.core.ModelUtil"
    val cleanName = cleanDataType(fqcn)
    cleanName should equal (fqcn)
  }

  it should "return a container with a fully-qualified inner class name unchanged" in {
    val fqcn = "List[com.wordnik.swagger.core.ModelUtil]"
    val cleanName = cleanDataType(fqcn)
    cleanName should equal (fqcn)
  }
}