import io.swagger.models.RefModel
import io.swagger.models.properties.RefProperty
import io.swagger.models.refs.RefFormat
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Helmsdown on 7/1/15.
  */
@RunWith(classOf[JUnitRunner])
class RefPropertyTest extends FlatSpec with Matchers {

   def assertRefFormat(ref: RefProperty, expectedFormat: RefFormat): Unit = {
     ref.getRefFormat should be (expectedFormat)
   }

   it should "correctly identify ref formats" in {
     assertRefFormat(new RefProperty("http://my.company.com/models/model.json"), RefFormat.URL);
     assertRefFormat(new RefProperty("http://my.company.com/models/model.json#/thing"), RefFormat.URL);
     assertRefFormat(new RefProperty("./models/model.json"), RefFormat.RELATIVE);
     assertRefFormat(new RefProperty("./models/model.json#/thing"), RefFormat.RELATIVE);
     assertRefFormat(new RefProperty("#/definitions/foo"), RefFormat.INTERNAL);
     assertRefFormat(new RefProperty("foo"), RefFormat.INTERNAL);
   }
 }
