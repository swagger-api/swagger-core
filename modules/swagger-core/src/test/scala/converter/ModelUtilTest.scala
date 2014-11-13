package converter

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ModelUtilTest extends FlatSpec with Matchers {
  import com.wordnik.swagger.core.util.ModelUtil._
  "ModelUtil cleanDataType" should "strip the package from a class name" in {
    val fqcn = "com.wordnik.swagger.core.ModelUtil"
    val cleanName = cleanDataType(fqcn)
    cleanName should equal (fqcn)
  }
}