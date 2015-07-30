import java.util.HashMap

import io.swagger.jaxrs.utils.PathUtils
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FlatSpec}

@RunWith(classOf[JUnitRunner])
class PathUtilsTest extends FlatSpec with Matchers {
  it should "parse regex with slash inside it from issue 1153" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath("/{itemId: [0-9]{4}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{3}/[A-Za-z0-9]+}", regexMap);
    path should be("/{itemId}")
    regexMap.get("itemId") should be("[0-9]{4}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{2}/[0-9]{3}/[A-Za-z0-9]+")
  }

  it should "parse two part path with one param" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath("/api/{itemId: [0-9]{4}/[0-9]{2,4}/[A-Za-z0-9]+}", regexMap);
    path should be("/api/{itemId}")
    regexMap.get("itemId") should be("[0-9]{4}/[0-9]{2,4}/[A-Za-z0-9]+")
  }

  it should "parse two part path with two params and white spaces around" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath("/{itemId: [0-9]{4}/[A-Za-z0-9]+}/{ api : [aA-zZ]+ }", regexMap);
    path should be("/{itemId}/{api}")
    regexMap.get("itemId") should be("[0-9]{4}/[A-Za-z0-9]+")
    regexMap.get("api") should be("[aA-zZ]+")
  }

  it should "parse simple path" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath("/api/itemId", regexMap);
    path should be("/api/itemId")
    regexMap.size() should be(0)
  }

  it should "parse path with param without regex" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath("/api/{name}", regexMap);
    path should be("/api/{name}")
    regexMap.size() should be(0)
  }

  it should "parse path with two params in one part" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath("/{a:\\w+}-{b:\\w+}/c", regexMap);
    path should be("/{a}-{b}/c")
    regexMap.get("a") should be("\\w+")
    regexMap.get("b") should be("\\w+")
  }

  it should "parse path like /swagger.{json|yaml}" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath("/swagger.{json|yaml}", regexMap);
    path should be("/swagger.{json|yaml}")
    regexMap.size() should be(0)
  }

  it should "parse path with many braces and slashes iside" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath("/api/{regex:/(?!\\{\\})\\w*|/\\{\\w+:*([^\\{\\}]*(\\{.*\\})*)*\\}}", regexMap);
    path should be("/api/{regex}")
    regexMap.get("regex") should be("/(?!\\{\\})\\w*|/\\{\\w+:*([^\\{\\}]*(\\{.*\\})*)*\\}")
  }

  it should "not fail when passed path is null" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath(null, regexMap);
    path should be(null)
  }

  it should "not fail when regex is not valid" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath("/api/{fail: [a-z]", regexMap);
    path should be(null)
  }

  it should "not fail when passed path is empty" in {
    val regexMap = new HashMap[String, String]();
    val path = PathUtils.parsePath("", regexMap);
    path should be("/")
  }
}
