import io.swagger.models.RefModel
import io.swagger.models.refs.{RefFormat, RefType, GenericRef}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by Helmsdown on 7/1/15.
 */
@RunWith(classOf[JUnitRunner])
class RefModelTest extends FlatSpec with Matchers {

  def assertRefFormat(ref: RefModel, expectedFormat: RefFormat): Unit = {
    ref.getRefFormat should be (expectedFormat)
  }

  it should "correctly identify ref formats" in {
    assertRefFormat(new RefModel("http://my.company.com/models/model.json"), RefFormat.URL);
    assertRefFormat(new RefModel("http://my.company.com/models/model.json#/thing"), RefFormat.URL);
    assertRefFormat(new RefModel("./models/model.json"), RefFormat.RELATIVE);
    assertRefFormat(new RefModel("./models/model.json#/thing"), RefFormat.RELATIVE);
    assertRefFormat(new RefModel("#/definitions/foo"), RefFormat.INTERNAL);
    assertRefFormat(new RefModel("foo"), RefFormat.INTERNAL);
  }
}
