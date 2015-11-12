package play.modules.swagger

import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.core.SwaggerContext
import com.wordnik.swagger.config._

import play.api.Logger
import play.core.Router.Routes

/**
 * Identifies Play Controllers annotated as Swagger API's.
 * Uses the Play Router to identify Controllers, and then tests each for the API annotation.
 */
class PlayApiScanner(routes: Option[Routes]) extends Scanner {

  def classes(): List[Class[_]] = {
    Logger("swagger").info("ControllerScanner - looking for controllers with API annotation")

    // get controller names from application routes
    val controllers = (routes match {
      case Some(r) => {
        for (doc <- r.documentation) yield {
          Logger("swagger").debug("route: " + doc.toString())
          val m1 = doc._3.lastIndexOf("(") match {
            case i: Int if (i > 0) => doc._3.substring(0, i)
            case _ => doc._3
          }
          Some(m1.substring(0, m1.lastIndexOf(".")).replace("@", ""))
        }
      }
      case None => Seq(None)
    }).flatten.distinct.toList

    controllers.collect {
      case className: String if {
        try {
          SwaggerContext.loadClass(className).getAnnotation(classOf[Api]) != null
        } catch {
          case ex: Exception => {
            Logger("swagger").error("Problem loading class:  %s. %s: %s".format(className, ex.getClass.getName, ex.getMessage))
            false}
        }
      } =>
        Logger("swagger").info("Found API controller:  %s".format(className))
        SwaggerContext.loadClass(className)
    }

  }
}
