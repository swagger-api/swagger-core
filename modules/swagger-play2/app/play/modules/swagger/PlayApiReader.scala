package play.modules.swagger

import java.lang.annotation.Annotation
import java.lang.reflect.Method
import javax.ws.rs._
import javax.ws.rs.core.Context

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.config._
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.jaxrs.{JaxrsApiReader, MutableParameter}
import com.wordnik.swagger.model.{ApiDescription, ApiListing, Operation, Parameter, ResponseMessage}
import play.api.Logger
import play.api.routing.Router

import scala.collection.mutable.ListBuffer

case class RouteEntry(httpMethod: String, path: String)

object SwaggerUtils {
  def convertPathString(str: String) = {
    str.replaceAll( """<\[[\^/\d-\w]*\]\+>""", "}").replaceAll("\\$", "{")
  }
}

class PlayApiReader(val router: Router) extends JaxrsApiReader {
  private var _routesCache: Map[String, RouteEntry] = null

  override
  def readRecursive(
    docRoot: String, 
    parentPath: String, cls: Class[_], 
    config: SwaggerConfig,
    operations: ListBuffer[(String, String, ListBuffer[Operation])],
    parentMethods: ListBuffer[Method]): Option[ApiListing] = {
    val api = cls.getAnnotation(classOf[Api])

    // must have @Api annotation to process!
    if(api != null) {
      val consumes = Option(api.consumes) match {
        case Some(e) if e != "" => e.split(",").map(_.trim).toList
        case _ => cls.getAnnotation(classOf[Consumes]) match {
          case e: Consumes => e.value.toList
          case _ => List()
        }
      }
      val produces = Option(api.produces) match {
        case Some(e) if e != "" => e.split(",").map(_.trim).toList
        case _ => cls.getAnnotation(classOf[Produces]) match {
          case e: Produces => e.value.toList
          case _ => List()
        }
      }
      val protocols = Option(api.protocols) match {
        case Some(e) if e != "" => e.split(",").map(_.trim).toList
        case _ => List()
      }
      val description = api.description match {
        case e: String if e != "" => Some(e)
        case _ => None
      }
      // look for method-level annotated properties
      val params = new ListBuffer[Parameter]
      for(field <- getAllFields(cls)) {
        // only process fields with @ApiParam, @QueryParam, @HeaderParam, @PathParam
        if(field.getAnnotation(classOf[QueryParam]) != null || field.getAnnotation(classOf[HeaderParam]) != null ||
          field.getAnnotation(classOf[HeaderParam]) != null || field.getAnnotation(classOf[PathParam]) != null ||
          field.getAnnotation(classOf[ApiParam]) != null) { 
          val param = new MutableParameter
          param.dataType = field.getType.getName
          Option (field.getAnnotation(classOf[ApiParam])) match {
            case Some(annotation) => toAllowableValues(annotation.allowableValues)
            case _ =>
          }
          val annotations = field.getAnnotations
          params ++= processParamAnnotations(param, annotations)
        }
      }

      val parentParams: List[Parameter] = params.toList

      for(method <- cls.getMethods) {
        val returnType = findSubresourceType(method)
        val path = method.getAnnotation(classOf[Path]) match {
          case e: Path => e.value()
          case _ => ""
        }
        val endpoint = (parentPath + pathFromMethod(method)).replace("//", "/")
        Option(returnType.getAnnotation(classOf[Api])) match {
          case Some(e) =>
            val root = docRoot + api.value + pathFromMethod(method)
            parentMethods += method
            readRecursive(root, endpoint, returnType, config, operations, parentMethods)
            parentMethods -= method
          case _ =>
            if(method.getAnnotation(classOf[ApiOperation]) != null) {
              // FIXME: this will always fail!
              readMethod(method, parentParams, parentMethods) match {
                case Some(op) => appendOperation(endpoint, path, op, operations)
                case None =>
              }
            }
        }
      }
      // sort them by min position in the operations
      val s = (for(op <- operations) yield {
        (op, op._3.map(_.position).toList.min)
      }).sortWith(_._2 < _._2).toList
      val orderedOperations = new ListBuffer[(String, String, ListBuffer[Operation])]
      s.foreach(op => {
        val ops = op._1._3.sortWith(_.position < _.position)
        orderedOperations += Tuple3(op._1._1, op._1._2, ops)
      })
      val apis = (for ((endpoint, resourcePath, operationList) <- orderedOperations) yield {
        val orderedOperations = new ListBuffer[Operation]
        operationList.sortWith(_.position < _.position).foreach(e => orderedOperations += e)
        ApiDescription(
          addLeadingSlash(endpoint),
          None,
          orderedOperations.toList)
      }).toList
      val models = ModelUtil.modelsFromApis(apis)
      Some(ApiListing (
        apiVersion = config.apiVersion,
        swaggerVersion = config.swaggerVersion,
        basePath = config.basePath,
        resourcePath = addLeadingSlash(api.value),
        apis = ModelUtil.stripPackages(apis),
        models = models,
        description = description,
        produces = produces,
        consumes = consumes,
        protocols = protocols,
        position = api.position)
      )
    }
    else None
  }

  override def read(docRoot: String, cls: Class[_], config: SwaggerConfig): Option[ApiListing] = {
    Logger("swagger").debug("ControllerReader: read(docRoot = %s, cls = %s, config = %s)".format(docRoot, cls.getName, config.toString))
    val api = cls.getAnnotation(classOf[Api])
    // must have @Api annotation to process!
    if (api != null) {
      Logger("swagger").debug("annotation: Api: %s,".format(api.toString))
      val resourcePath = api.value

      val consumes = Option(api.consumes) match {
        case Some(e) if e != "" => e.split(",").map(_.trim).toList
        case _ => cls.getAnnotation(classOf[Consumes]) match {
          case e: Consumes => e.value.toList
          case _ => List()
        }
      }

      // assume json as default
      val produces = Option(api.produces) match {
        case Some(e) if e != "" => e.split(",").map(_.trim).toList
        case _ => cls.getAnnotation(classOf[Produces]) match {
          case e: Produces => e.value.toList
          case _ => List("application/json")
        }
      }

      val protocols = Option(api.protocols) match {
        case Some(e) if e != "" => e.split(",").map(_.trim).toList
        case _ => List()
      }

      val description = api.description match {
        case e: String if e != "" => Some(e)
        case _ => None
      }

      // define a Map to hold Operations keyed by resourcepath
      var operationsMap: Map[String, List[Operation]] = Map.empty
      for (method <- cls.getMethods) {
        // only process methods with @ApiOperation annotations
        if (method.getAnnotation(classOf[ApiOperation]) != null) {
          Logger("swagger").debug("ApiOperation: found on method: %s".format(method.getName))
          val fullMethodName = getFullMethodName(cls, method)
          routesCache.get(fullMethodName) match {
            case Some(RouteEntry(httpMethod, path)) => {
               // got to remove any path element specified in basepath
              val operation = readMethod(method, fullMethodName).get
              val basepathUrl = new java.net.URL(config.getBasePath)
              val basepath = basepathUrl.getPath
              val resourcePath = path.stripPrefix(basepath)
              Logger("swagger").debug("method: %s, fullOperationResourcePath: %s, basepath: %s, resourcePath: %s".format(method.getName, path, basepath, resourcePath))
              // store operations in our Map keyed by resourcepath
              operationsMap = appendOperation(resourcePath, operation, operationsMap)
            }
            case _ =>
              Logger("swagger").debug("Method: %s has no web route defined".format(method.getName))
          }
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
          config.getSwaggerVersion,
          config.basePath,
          resourcePath,
          produces,
          consumes,
          protocols,
          List(),
          ModelUtil.stripPackages(apiDescriptions), //  List[com.wordnik.swagger.model.ApiDescription]
          models,
          description
          // position
        )
      )
    }
    else {
      Logger("swagger").debug("No Api annotation found for class %s".format(cls.getName))
      None
    }
  }

  def readMethod(method: Method, fullMethodName: String): Option[Operation] = {
    val apiOperation = method.getAnnotation(classOf[ApiOperation])
    val routeEntry = routesCache.get(fullMethodName)

    if (method.getAnnotation(classOf[ApiOperation]) != null) {
      Logger("swagger").debug("annotation: ApiOperation: %s,".format(apiOperation.toString))

      val httpMethod = routeEntry.map(_.httpMethod).getOrElse(apiOperation.httpMethod)

      val nickname = apiOperation.nickname match {
        case e: String if e.trim != "" => e
        case _ => genNickname(fullMethodName, routeEntry.map(_.httpMethod))
      }

      val produces = apiOperation.produces match {
        case e: String if e.trim != "" => e.split(",").map(_.trim).toList
        case _ => List()
      }

      val consumes = apiOperation.consumes match {
        case e: String if e.trim != "" => e.split(",").map(_.trim).toList
        case _ => List()
      }
      val protocols = apiOperation.protocols match {
        case e: String if e.trim != "" => e.split(",").map(_.trim).toList
        case _ => List()
      }
      val authorizations: List[com.wordnik.swagger.model.Authorization] = Option(apiOperation.authorizations) match {
        case Some(e) => (for(a <- e) yield {
          val scopes = (for(s <- a.scopes) yield com.wordnik.swagger.model.AuthorizationScope(s.scope, s.description)).toArray
          if(a.value != null && !"".equals(a.value))
            Some(new com.wordnik.swagger.model.Authorization(a.value, scopes))
          else
            None
        }).flatten.toList
        case _ => List()
      }
      val responseClass = apiOperation.responseContainer match {
        case "" => apiOperation.response.getName
        case e: String => "%s[%s]".format(e, apiOperation.response.getName)
      }

      val responseAnnotations = method.getAnnotation(classOf[ApiResponses])

      val apiResponses = processResponsesAnnotation(responseAnnotations)

      val isDeprecated = Option(method.getAnnotation(classOf[Deprecated])).map(m => "true").orNull

      val implicitParams = processImplicitParams(method)

      val params = processParams(method)

      Some(Operation(
        httpMethod,
        apiOperation.value,
        apiOperation.notes,
        responseClass,
        nickname,
        apiOperation.position,
        produces,
        consumes,
        protocols,
        authorizations,
        params ++ implicitParams,
        apiResponses,
        Option(isDeprecated)))
    } else {
      None
    }
  }

  def processImplicitParams(method: Method) = {
    Logger("swagger").debug("checking for ApiImplicitParams")
    Option(method.getAnnotation(classOf[ApiImplicitParams])) match {
      case Some(e) =>
        (for (param <- e.value) yield {
          Logger("swagger").debug("processing " + param)
          val dataType = if (param.dataType.isEmpty) "String" else param.dataType
          val allowableValues = toAllowableValues(param.allowableValues)
          Parameter(
            param.name,
            Option(readString(param.value)),
            Option(param.defaultValue).filter(_.trim.nonEmpty),
            param.required,
            param.allowMultiple,
            dataType,
            allowableValues,
            param.paramType,
            Option(param.access).filter(_.trim.nonEmpty))
        }).toList
      case _ => List()
    }
  }

  def processParams(method: Method): List[Parameter] = {
    Logger("swagger").debug("checking for ApiParams")
    val paramAnnotations = method.getParameterAnnotations
    val paramTypes = method.getParameterTypes
    val genericParamTypes: Array[java.lang.reflect.Type] = method.getGenericParameterTypes

    val paramList = new ListBuffer[Parameter]
    for ((annotations, paramType, genericParamType) <- (paramAnnotations, paramTypes, genericParamTypes).zipped.toList) yield {
      if (annotations.length > 0) {
        val param = new MutableParameter
        param.dataType = processDataType(paramType, genericParamType)
        param.allowableValues = processAllowableValues(paramType, genericParamType)
        paramList ++= processParamAnnotations(param, annotations)
      }
    }

    paramList.toList
  }

  def processResponsesAnnotation(responseAnnotations: ApiResponses) = {
    if (responseAnnotations == null) List()
    else (for (response <- responseAnnotations.value) yield {
      val apiResponse = {
        if (response.response != classOf[Void])
          Some(response.response.getName)
        else None
      }
      ResponseMessage(response.code, response.message, apiResponse)
    }).toList
  }

  def processParamAnnotations(mutable: MutableParameter, paramAnnotations: Array[Annotation]): List[Parameter] = {
    var shouldIgnore = false
    for (pa <- paramAnnotations) {
      pa match {
        case e: ApiParam => parseApiParamAnnotation(mutable, e)
        case e: QueryParam =>
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_QUERY, mutable.paramType)
        case e: PathParam =>
          mutable.name = readString(e.value, mutable.name)
          mutable.required = true
          mutable.paramType = readString(TYPE_PATH, mutable.paramType)
        case e: MatrixParam =>
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_MATRIX, mutable.paramType)
        case e: HeaderParam =>
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_HEADER, mutable.paramType)
        case e: FormParam =>
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_FORM, mutable.paramType)
        case e: CookieParam =>
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_COOKIE, mutable.paramType)
        case e: DefaultValue =>
          mutable.defaultValue = Option(readString(e.value))
        case e: Context => shouldIgnore = true
        case _ =>
      }
    }
    if (!shouldIgnore) {
      if (mutable.paramType == null) {
        mutable.paramType = TYPE_BODY
        mutable.name = TYPE_BODY
      }
      List(mutable.asParameter())
    }
    else List()
  }

  def appendOperation(resourcePath: String, operation: Operation, operationsMap: Map[String, List[Operation]]): Map[String, List[Operation]] = {
    operationsMap.get(resourcePath) match {
      case Some(e) =>
        operationsMap - resourcePath + (resourcePath -> e.+:(operation))
      case None =>
        operationsMap + (resourcePath -> List(operation))
    }
  }

  def routesCache = synchronized {
    if (_routesCache == null) _routesCache = populateRoutesCache
    _routesCache
  }

  /**
   * Get the path for a given method
   */
  def getPath(clazz: Class[_], method: Method): Option[String] = {
    val fullMethodName = getFullMethodName(clazz, method)
    if (routesCache.contains(fullMethodName)) {
      val path = routesCache(fullMethodName).path
      Logger("swagger").debug("PlayApiReader.getPath: str = %s,".format(path))
      Some(path)
    } else {
      Logger warn "Cannot determine Path. Nothing defined in play routes file for api method " + method.toString
      None
    }
  }

  def getFullMethodName(clazz: Class[_], method: Method): String = {
    clazz.getCanonicalName.indexOf("$") match {
      case -1 => clazz.getCanonicalName + "$." + method.getName
      case _ => clazz.getCanonicalName + "." + method.getName
    }
  }

  def genNickname(fullMethodName: String, httpMethod: Option[String] = None): String = {
    httpMethod.getOrElse("") + "_" + fullMethodName.replace(".", "_")
  }

  private def populateRoutesCache: Map[String, RouteEntry] = {
    val r = router.documentation
    (for (route <- r) yield {
      val httpMethod = route._1
      val path = SwaggerUtils.convertPathString(route._2)
      val routeName = {
        // extract the args in parens
        val fullMethod = route._3 match {
          case x if x.indexOf("(") > 0 => x.substring(0, x.indexOf("("))
          case _ => route._3
        }
        val idx = fullMethod.lastIndexOf(".")
        (fullMethod.substring(0, idx) + "$." + fullMethod.substring(idx + 1)).replace("@", "")
      }
      Logger("swagger").debug("Add to route cache:  name: %s, method: %s, path: %s".format(routeName, httpMethod, path))
      (routeName, RouteEntry(httpMethod, path))
    }).toMap
  }

  /**
   * Do not be tempted to use this.... this is here to cover the method in the superclass.
   * @param method
   * @param parentParams
   * @param parentMethods
   * @return
   * @deprecated - do not be tempted to use this.... this is here to cover the method in the superclass.
   */
  @Deprecated
  override def readMethod(method: Method, parentParams : List[Parameter], parentMethods : ListBuffer[Method]) : Option[Operation] = {
   // don't use this - it is specific to Jax-RS models.
    throw new RuntimeException("method not in use..")
    None
  }

  def findSubresourceType(method: Method): Class[_] = {
    method.getReturnType
  }
}

