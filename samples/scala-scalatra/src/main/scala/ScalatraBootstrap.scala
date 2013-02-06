import com.wordnik.swagger.sample.api._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
	implicit val swagger = new PetstoreSwagger

  override def init(context: ServletContext) {
    try {
    	context.mount(new ResourcesApp, "/api-docs/*")
      context.mount(new PetstoreApi, "/pet/*")
    } catch {
      case t: Exception => println(t)
    }
  }
}
