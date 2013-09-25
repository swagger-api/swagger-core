package play.modules.swagger

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.config._
import com.wordnik.swagger.reader.{ClassReader, ClassReaderUtils}
import com.wordnik.swagger.core.util._

import java.lang.reflect.Method

import scala.collection.mutable.ListBuffer
import play.api.{Configuration, Logger}

import com.wordnik.swagger.model.Parameter
import com.wordnik.swagger.model.ApiDescription
import scala.Some
import com.wordnik.swagger.model.Operation
import com.wordnik.swagger.model.ResponseMessage
import com.wordnik.swagger.model.ApiListing
import play.core.Router.Routes


case class RouteEntry(httpMethod: String, path: String)

object SwaggerUtils {
  def convertPathString(str: String) = {
    str.replaceAll( """<\[[\^/\d-\w]*\]\+>""", "}").replaceAll("\\$", "{")
  }
}

class PlayApiReader(routes: Option[Routes], config: Configuration) extends ClassReader with ClassReaderUtils {

  private var _routesCache: Map[String, RouteEntry] = null

  private val formatString = config.getString("swagger.api.format.string") match {
    case Some(str) => str
    case _ => ".{format}"
  }

  def read(docRoot: String, cls: Class[_], config: SwaggerConfig): Option[ApiListing] = {
    Logger("swagger").debug("ControllerReader: read(docRoot = %s, cls = %s, config = %s)".format(docRoot, cls.getName, config.toString))
    val api = cls.getAnnotation(classOf[Api])
    Logger("swagger").debug("annotation: Api: %s,".format(api.toString))
    if (api != null) {

      val resourcePath = api.value

      val description = api.description match {
        case str: String => Some(str)
        case _ => None
      }

      // define a Map to hold Operations keyed by resourcepath
      var operationsMap: Map[String, List[Operation]] = Map.empty

      for (method <- cls.getMethods) {
        // only process methods with @ApiOperation annotations
        if (method.getAnnotation(classOf[ApiOperation]) != null) {
          Logger("swagger").debug("ApiOperation: found on method: %s".format(method.getName))

          val operation = readMethod(method).get


          val fullOperationResourcePath = getPath(cls, method)
          // got to remove any path element specified in basepath

          val basepathUrl = new java.net.URL(config.getBasePath)
          val basepath = basepathUrl.getPath


          val resourcePath = fullOperationResourcePath.stripPrefix(basepath)
          Logger("swagger").debug("method: %s, fullOperationResourcePath: %s, basepath: %s, resourcePath: %s".format(method.getName, fullOperationResourcePath, basepath, resourcePath))

          // store operations in our Map keyed by resourcepath
          val opsList: List[Operation] = operationsMap.getOrElse(resourcePath, List.empty[Operation])
          val newOpsList = opsList.+:(operation)
          operationsMap = operationsMap - resourcePath
          operationsMap = operationsMap + (resourcePath -> newOpsList)

        }
      }

      val apiDescriptions: List[ApiDescription] = operationsMap.map {
        case (resourcePath, operations) =>
          ApiDescription(resourcePath, None, operations)
      }.toList

      val models = ModelUtil.modelsFromApis(apiDescriptions)

      Some(
        ApiListing(
          config.apiVersion,
          config.getSwaggerVersion(),
          config.basePath,
          resourcePath, //resourcePath,
          List("application/json"), // produces
          List(), // consumes
          List(), // protocols
          List(), // authorizations
          ModelUtil.stripPackages(apiDescriptions), //  List[com.wordnik.swagger.model.ApiDescription]
          models, // models
          description
          // position
        )
      )
    }
    else None
  }

  def routesCache = {
    if (_routesCache == null) _routesCache = populateRoutesCache
    _routesCache
  }

  def readMethod(method: Method): Option[Operation] = {
    Logger("swagger").debug("ApiOperation: found on method: %s".format(method.getName))

    if (method.getAnnotation(classOf[ApiOperation]) != null) {

      // process only @ApiImplicitParams
      val parameters = {
        val paramListAnnotation = method.getAnnotation(classOf[ApiImplicitParams])
        if (paramListAnnotation != null) {
          (for (param <- paramListAnnotation.value) yield {
            val allowableValues = toAllowableValues(param.allowableValues)
            Parameter(
              param.name,
              None,
              Option(param.defaultValue).filter(_.trim.nonEmpty),
              param.required,
              param.allowMultiple,
              param.dataType,
              allowableValues,
              param.paramType,
              Option(param.access).filter(_.trim.nonEmpty))
          }).toList
        }
        else List()
      }
      val opa = method.getAnnotation(classOf[ApiOperation])
      Logger("swagger").debug("annotation: ApiOperation: %s,".format(opa.toString))
      val produces = opa.produces match {
        case e: String if (e != "") => e.split(",").map(_.trim).toList
        case _ => List()
      }
      val consumes = opa.consumes match {
        case e: String if (e != "") => e.split(",").map(_.trim).toList
        case _ => List()
      }
      val protocols = opa.protocols match {
        case e: String if (e != "") => e.split(",").map(_.trim).toList
        case _ => List()
      }
      val authorizations = opa.authorizations match {
        case e: String if (e != "") => e.split(",").map(_.trim).toList
        case _ => List()
      }
      val responseClass = opa.responseContainer match {
        case "" => opa.response.getName
        case e: String => "%s[%s]".format(e, opa.response.getName)
      }
      val responseAnnotations = method.getAnnotation(classOf[ApiResponses])
      val apiResponses = {
        if (responseAnnotations == null) List()
        else (for (response <- responseAnnotations.value) yield {
          val apiResponse = {
            if (response.response != classOf[Void])
              Some(response.response.getName)
            else None
          }
          ResponseMessage(response.code, response.message, apiResponse)
        }
          ).toList
      }

      Some(Operation(
        opa.httpMethod,
        opa.value,
        opa.notes,
        responseClass,
        opa.nickname,
        opa.position,
        produces,
        consumes,
        protocols,
        authorizations,
        parameters,
        apiResponses,
        None))
    } else {
      None
    }
  }

  /**
   * Get the path for a given method
   */
  def getPath(clazz: Class[_], method: Method) = {
    val fullMethodName = getFullMethodName(clazz, method)
    val str = {
      routesCache.contains(fullMethodName) match {
        case true => routesCache(fullMethodName).path
        case false => {
          Logger error "Cannot determine Path. Nothing defined in play routes file for api method " + method.toString
          //this.resourcePath
          // todo work out what to do here
          "xxxxxxxx"
        }
      }
    }

    val s = formatString match {
      case "" => str
      case e: String => str.replaceAll(".json", formatString).replaceAll(".xml", formatString)
    }
    Logger("swagger").debug("PlayApiReader.getPath: str = %s,".format(s))
    s
  }

  def getFullMethodName(clazz: Class[_], method: Method): String = {
    clazz.getCanonicalName.indexOf("$") match {
      case -1 => clazz.getCanonicalName + "$." + method.getName
      case _ => clazz.getCanonicalName + "." + method.getName
    }
  }

  private def populateRoutesCache: Map[String, RouteEntry] = {

    val r = play.api.Play.current.routes.get.documentation

    (for (route <- r) yield {
      val httpMethod = route._1
      val path = SwaggerUtils.convertPathString(route._2)
      val routeName = {
        // extract the args in parens
        val fullMethod = route._3 match {
          case x if (x.indexOf("(") > 0) => x.substring(0, x.indexOf("("))
          case _ => route._3
        }
        val idx = fullMethod.lastIndexOf(".")
        (fullMethod.substring(0, idx) + "$." + fullMethod.substring(idx + 1)).replace("@", "")
      }
      Logger("swagger").debug("Add to route cache:  name: %s, method: %s, path: %s".format(routeName, httpMethod, path))
      (routeName, RouteEntry(httpMethod, path))
    }).toMap
  }

}
