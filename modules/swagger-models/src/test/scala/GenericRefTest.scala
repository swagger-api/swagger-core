import io.swagger.models.refs.{RefFormat, RefType, GenericRef}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by Helmsdown on 7/1/15.
 */
@RunWith(classOf[JUnitRunner])
class GenericRefTest extends FlatSpec with Matchers {

  def assertRefFormat(ref: GenericRef, expectedFormat: RefFormat): Unit = {
    ref.getFormat should be (expectedFormat)
  }

  it should "correctly indentify URL refs" in {
    assertRefFormat(new GenericRef(RefType.DEFINITION, "http://my.company.com/models/model.json"), RefFormat.URL);
    assertRefFormat(new GenericRef(RefType.DEFINITION, "http://my.company.com/models/model.json#/thing"), RefFormat.URL);
    assertRefFormat(new GenericRef(RefType.PARAMETER, "http://my.company.com/models/model.json"), RefFormat.URL);
    assertRefFormat(new GenericRef(RefType.PARAMETER, "http://my.company.com/models/model.json#/thing"), RefFormat.URL);
  }
  
  it should "correctly identify internal refs" in {
    assertRefFormat(new GenericRef(RefType.DEFINITION, "#/definitions/foo"), RefFormat.INTERNAL);
    assertRefFormat(new GenericRef(RefType.PARAMETER, "#/parameters/foo"), RefFormat.INTERNAL);
    assertRefFormat(new GenericRef(RefType.PARAMETER, "Foo"), RefFormat.INTERNAL);
    assertRefFormat(new GenericRef(RefType.DEFINITION, "Foo"), RefFormat.INTERNAL);
  }

  it should "correctly identify relative refs" in {
    assertRefFormat(new GenericRef(RefType.DEFINITION, "./path/to/model.json"), RefFormat.RELATIVE);
    assertRefFormat(new GenericRef(RefType.DEFINITION, "./path/to/model.json#/thing"), RefFormat.RELATIVE);
    assertRefFormat(new GenericRef(RefType.DEFINITION, "../path/to/model.json"), RefFormat.RELATIVE);
    assertRefFormat(new GenericRef(RefType.DEFINITION, "../path/to/model.json#/thing"), RefFormat.RELATIVE);
    assertRefFormat(new GenericRef(RefType.PARAMETER, "./path/to/model.json"), RefFormat.RELATIVE);
    assertRefFormat(new GenericRef(RefType.PARAMETER, "./path/to/model.json#/thing"), RefFormat.RELATIVE);
    assertRefFormat(new GenericRef(RefType.PARAMETER, "../path/to/model.json"), RefFormat.RELATIVE);
    assertRefFormat(new GenericRef(RefType.PARAMETER, "../path/to/model.json#/thing"), RefFormat.RELATIVE);
  }

  def assertRefStringIsUnchanged(refType: RefType, refStr: String): Unit = {
    assertRefString(refType, refStr, refStr)
  }

  def assertRefString(refType: RefType, refStr: String, expectedRefStr: String): Unit = {
    val refObj = new GenericRef(refType, refStr)
    refObj.getRef should be (expectedRefStr)
  }

  def assertSimpleRefMatchesRef(refType: RefType, refStr: String): Unit = {
    assertSimpleRef(refType, refStr, refStr)
  }

  def assertSimpleRef(refType: RefType, refStr: String, expectedSimpleRef: String): Unit = {
    val refObj = new GenericRef(refType, refStr)
    refObj.getSimpleRef should be (expectedSimpleRef)
  }


  it should "give back the right ref string" in {
    assertRefStringIsUnchanged(RefType.DEFINITION, "./path/to/model.json")
    assertRefStringIsUnchanged(RefType.DEFINITION, "./path/to/model.json#/thing")
    assertRefStringIsUnchanged(RefType.PARAMETER, "./path/to/parameters.json#/param")
    assertRefStringIsUnchanged(RefType.PARAMETER, "./path/to/parameters.json#/param")
    assertRefStringIsUnchanged(RefType.DEFINITION, "#/definitions/foo")
    assertRefStringIsUnchanged(RefType.PARAMETER, "#/parameters/foo")
    assertRefStringIsUnchanged(RefType.DEFINITION, "http://my.company.com/models/model.json")
    assertRefStringIsUnchanged(RefType.DEFINITION, "http://my.company.com/models/model.json#/thing")
    assertRefStringIsUnchanged(RefType.PARAMETER, "http://my.company.com/models/model.json")
    assertRefStringIsUnchanged(RefType.PARAMETER, "http://my.company.com/models/model.json#/thing")

    assertRefString(RefType.DEFINITION, "foo", "#/definitions/foo")
    assertRefString(RefType.PARAMETER, "foo", "#/parameters/foo")
  }
  
  it should "give back the right simple ref" in {
    assertSimpleRefMatchesRef(RefType.DEFINITION, "./path/to/model.json")
    assertSimpleRefMatchesRef(RefType.DEFINITION, "./path/to/model.json#/thing")
    assertSimpleRefMatchesRef(RefType.PARAMETER, "./path/to/parameters.json#/param")
    assertSimpleRefMatchesRef(RefType.PARAMETER, "./path/to/parameters.json#/param")

    assertSimpleRefMatchesRef(RefType.DEFINITION, "http://my.company.com/models/model.json")
    assertSimpleRefMatchesRef(RefType.DEFINITION, "http://my.company.com/models/model.json#/thing")
    assertSimpleRefMatchesRef(RefType.PARAMETER, "http://my.company.com/models/model.json")
    assertSimpleRefMatchesRef(RefType.PARAMETER, "http://my.company.com/models/model.json#/thing")

    assertSimpleRef(RefType.DEFINITION, "#/definitions/foo", "foo")
    assertSimpleRef(RefType.PARAMETER, "#/parameters/foo", "foo")
    assertSimpleRef(RefType.DEFINITION, "foo", "foo")
    assertSimpleRef(RefType.PARAMETER, "foo", "foo")
  } 


}
