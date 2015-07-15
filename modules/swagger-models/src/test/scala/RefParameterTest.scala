import io.swagger.models.parameters.RefParameter
import io.swagger.models.properties.RefProperty
import io.swagger.models.refs.RefFormat
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Helmsdown on 7/1/15.
  */
@RunWith(classOf[JUnitRunner])
class RefParameterTest extends FlatSpec with Matchers {

   def assertRefFormat(ref: RefParameter, expectedFormat: RefFormat): Unit = {
     ref.getRefFormat should be (expectedFormat)
   }

   it should "correctly identify ref formats" in {
     assertRefFormat(new RefParameter("http://my.company.com/models/model.json"), RefFormat.URL);
     assertRefFormat(new RefParameter("http://my.company.com/models/model.json#/thing"), RefFormat.URL);
     assertRefFormat(new RefParameter("./models/model.json"), RefFormat.RELATIVE);
     assertRefFormat(new RefParameter("./models/model.json#/thing"), RefFormat.RELATIVE);
     assertRefFormat(new RefParameter("#/parameters/foo"), RefFormat.INTERNAL);
     assertRefFormat(new RefParameter("foo"), RefFormat.INTERNAL);
   }
 }
