package reader

import testdata._

import com.wordnik.swagger.core.util.JsonSerializer
import com.wordnik.swagger.config.SwaggerConfig
import com.wordnik.swagger.servlet.config._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ServletReaderTest extends FlatSpec with Matchers {
  it should "read a servlet" in {
    val config = new SwaggerConfig
    config.basePath = "http://localhost:8080/api"
    config.apiVersion = "1.0.0"
    val reader = new ServletReader

    val o = reader.read("/", classOf[SampleServlet], config)
    println(JsonSerializer.asJson(o))
  }
}

