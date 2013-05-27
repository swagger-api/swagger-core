package com.wordnik.swagger.jaxrs

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter.ModelConverters
import com.wordnik.swagger.config._
import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.model._

import org.slf4j.LoggerFactory

import java.lang.reflect.{ Method, Type }
import java.lang.annotation.Annotation

import javax.ws.rs._
import javax.ws.rs.core.Context

import scala.collection.JavaConverters._
import scala.collection.mutable.{ ListBuffer, HashMap, HashSet }

object JaxrsApiReader {
  private val LOGGER = LoggerFactory.getLogger(JaxrsApiReader.getClass)

  val ComplexTypeMatcher = "([a-zA-Z]*)\\[([a-zA-Z\\.\\-]*)\\].*".r

  def read(docRoot: String, cls: Class[_], config: SwaggerConfig): Option[ApiListing] = {
    val api = cls.getAnnotation(classOf[Api])
    if(api != null) {
      val operations = new ListBuffer[Tuple3[String, String, ListBuffer[Operation]]]()
      val description = api.description match {
        case e: String if(e != "") => Some(e)
        case _ => None
      }

      for(method <- cls.getMethods) {
        val apiOperation = method.getAnnotation(classOf[ApiOperation])
        val errorAnnotations = method.getAnnotation(classOf[ApiErrors])
        val opPath = method.getAnnotation(classOf[Path]) match {
          case e: Path => e.value()
          case _ => ""
        }
        val errorResponses = {
          if(errorAnnotations == null) List()
          else (for(error <- errorAnnotations.value) yield {
            val errorResponse = {
              if(error.response != classOf[Void])
                Some(error.response.getName)
              else None
            }
            ErrorResponse(error.code, error.reason, errorResponse)}
          ).toList
        }
        val isDeprecated = Option(method.getAnnotation(classOf[Deprecated])).map(m => "true").getOrElse(null)

        if(apiOperation != null) {
          val responseClass = apiOperation.responseContainer match {
            case "" => apiOperation.response.getName
            case e: String => "%s[%s]".format(e, apiOperation.response.getName)
          }

          val paramAnnotations = method.getParameterAnnotations
          val paramTypes = method.getParameterTypes
          val genericParamTypes = method.getGenericParameterTypes

          val params = (for((annotations, paramType, genericParamType) <- (paramAnnotations, paramTypes, genericParamTypes).zipped.toList) yield {
            if(annotations.length > 0) {
              val param = new MutableParameter
              param.dataType = ModelConverters.toName(paramType)
              processParamAnnotations(param, annotations, method)
            }
            else None
          }).flatten.toList

          val endpoint = api.value + pathFromMethod(method)

          val op = Operation(
            parseHttpMethod(method, apiOperation),
            apiOperation.value,
            apiOperation.notes,
            responseClass,
            method.getName,
            apiOperation.position,
            params,
            errorResponses,
            Option(isDeprecated))

          val ops = operations.filter(op => op._1 == endpoint) match {
            case e: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]] if(e.size > 0) =>
              e.head._3 += op
            case _ => operations += Tuple3(endpoint, opPath, new ListBuffer[Operation]() ++= List(op))
          }
          Some(ops) 
        }
        // TODO: parse implicit param annotations
      }
      val apis = (for ((endpoint, resourcePath, operationList) <- operations) yield {
        val orderedOperations = new ListBuffer[Operation]
        operationList.sortWith(_.position < _.position).foreach(e => orderedOperations += e)

        ApiDescription(
          endpoint,
          None,
          orderedOperations.toList)
      }).toList

      val models = modelsFromApis(apis)
      Some(ApiListing (
        apiVersion = config.apiVersion,
        swaggerVersion = config.swaggerVersion,
        basePath = config.basePath,
        resourcePath = (docRoot + api.value),
        apis = stripPackages(apis),
        models = models,
        description = description)
      )
    }
    else None
  }

  def stripPackages(apis: List[ApiDescription]): List[ApiDescription] = {
    (for(api <- apis) yield {
      val operations = (for(op <- api.operations) yield {
        val parameters = (for(param <- op.parameters) yield {
          param.copy(dataType = cleanDataType(param.dataType))
        }).toList
        val errors = (for(error <- op.errorResponses) yield {
          if(error.responseModel != None) {
            error.copy(responseModel = Some(cleanDataType(error.responseModel.get)))
          }
          else error
        }).toList
        op.copy(
          responseClass = cleanDataType(op.responseClass),
          parameters = parameters,
          errorResponses = errors)
      }).toList
      api.copy(operations = operations)
    }).toList
  }

  def cleanDataType(dataType: String) = {
    val out = if(dataType.startsWith("java.lang")) {
      val trimmed = dataType.substring("java.lang".length + 1)
      if(SwaggerSpec.baseTypes.contains(trimmed.toLowerCase))
        trimmed.toLowerCase
      else
        trimmed
    }
    else {
      modelFromString(dataType) match {
        case Some(e) => e._1
        case None => dataType
      }
    }
    // put back in container
    dataType match {
      case e: String if(e.toLowerCase.startsWith("list")) => "List[%s]".format(out)
      case e: String if(e.toLowerCase.startsWith("set")) => "Set[%s]".format(out)
      case e: String if(e.toLowerCase.startsWith("array")) => "Array[%s]".format(out)
      case e: String if(e.toLowerCase.startsWith("map")) => "Map[string,%s]".format(out)
      case _ => out
    }
  }

  def modelsFromApis(apis: List[ApiDescription]): Option[Map[String, Model]] = {
    val modelnames = new HashSet[String]()
    for(api <- apis; op <- api.operations) {
      modelnames ++= op.errorResponses.map{_.responseModel}.flatten.toSet
      modelnames += op.responseClass
      op.parameters.foreach(param => modelnames += param.dataType)
    }
    val models = (for(name <- modelnames) yield modelAndDependencies(name)).flatten.toMap
    if(models.size > 0) Some(models)
    else None
  }

  def modelAndDependencies(name: String): Map[String, Model] = {
    val typeRef = name match {
      case ComplexTypeMatcher(containerType, basePart) => {
        if(basePart.indexOf(",") > 0) // handle maps, i.e. List[String,String]
          basePart.split("\\,").last.trim
        else basePart
      }
      case _ => name
    }
    if(shoudIncludeModel(typeRef)) {
      try{
        val cls = SwaggerContext.loadClass(typeRef)
        (for(model <- ModelConverters.readAll(cls)) yield (model.name, model)).toMap
      }
      catch {
        case e: ClassNotFoundException => Map()
      }
    }
    else Map()
  }

  def modelFromString(name: String): Option[Tuple2[String, Model]] = {
    val typeRef = name match {
      case ComplexTypeMatcher(containerType, basePart) => {
        if(basePart.indexOf(",") > 0) // handle maps, i.e. List[String,String]
          basePart.split("\\,").last.trim
        else basePart
      }
      case _ => name
    }
    if(shoudIncludeModel(typeRef)) {
      try{
        val cls = SwaggerContext.loadClass(typeRef)
        ModelConverters.read(cls) match {
          case Some(model) => Some((cls.getSimpleName, model))
          case None => None
        }
      }
      catch {
        case e: ClassNotFoundException => None
      }
    }
    else None
  }

  def shoudIncludeModel(modelname: String) = {
    if(SwaggerSpec.baseTypes.contains(modelname.toLowerCase))
      false
    else if(modelname.startsWith("java.lang"))
      false
    else true
  }

  def pathFromMethod(method: Method): String = {
    val path = method.getAnnotation(classOf[javax.ws.rs.Path])
    if(path == null) ""
    else path.value
  }

  // decorates a Parameter based on annotations, returns None if param should be ignored
  def processParamAnnotations(mutable: MutableParameter, paramAnnotations: Array[Annotation], method: Method): Option[Parameter] = {
    var shouldIgnore = false
    for (pa <- paramAnnotations) {
      pa match {
        case e: ApiParam => parseApiParamAnnotation(mutable, e, method)
        case e: QueryParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_QUERY, mutable.paramType)
        }
        case e: PathParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.required = true
          mutable.paramType = readString(TYPE_PATH, mutable.paramType)
        }
        case e: MatrixParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_MATRIX, mutable.paramType)
        }
        case e: HeaderParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_HEADER, mutable.paramType)
        }
        case e: FormParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_FORM, mutable.paramType)
        }
        case e: CookieParam => {
          mutable.name = readString(e.value, mutable.name)
          mutable.paramType = readString(TYPE_COOKIE, mutable.paramType)
        }
        case e: Context => shouldIgnore = true
        case _ =>
      }
    }
    if(!shouldIgnore) Some(mutable.asParameter)
    else None
  }

  def parseApiParamAnnotation(param: MutableParameter, annotation: ApiParam, method: Method) {
    param.name = readString(annotation.name, param.name)
    param.description = readString(annotation.value)
    param.defaultValue = readString(annotation.defaultValue)

    try {
      param.allowableValues = toAllowableValues(annotation.allowableValues)
    } catch {
      case e: Exception =>
        LOGGER.error("Allowable values annotation problem in method  " + method +
          "for parameter " + param.name)
    }
    param.required = annotation.required
    param.allowMultiple = annotation.allowMultiple
    param.paramAccess = Option(readString(annotation.access))
  }

  def toAllowableValues(csvString: String, paramType: String = null): AllowableValues = {
    if (csvString.toLowerCase.startsWith("range[")) {
      val ranges = csvString.substring(6, csvString.length() - 1).split(",")
      return buildAllowableRangeValues(ranges, csvString)
    } else if (csvString.toLowerCase.startsWith("rangeexclusive[")) {
      val ranges = csvString.substring(15, csvString.length() - 1).split(",")
      return buildAllowableRangeValues(ranges, csvString)
    } else {
      if (csvString == null || csvString.length == 0) {
        null
      } else {
        val params = csvString.split(",").toList
        paramType match {
          case null => new AllowableListValues(params)
          case "string" => new AllowableListValues(params)
        }
      }
    }
  }

  val POSITIVE_INFINITY_STRING = "Infinity"
  val NEGATIVE_INFINITY_STRING = "-Infinity"

  def buildAllowableRangeValues(ranges: Array[String], inputStr: String): AllowableRangeValues = {
    var min: java.lang.Float = 0
    var max: java.lang.Float = 0
    if (ranges.size < 2) {
      throw new RuntimeException("Allowable values format " + inputStr + "is incorrect")
    }
    if (ranges(0).equalsIgnoreCase(POSITIVE_INFINITY_STRING)) {
      min = Float.PositiveInfinity
    } else if (ranges(0).equalsIgnoreCase(NEGATIVE_INFINITY_STRING)) {
      min = Float.NegativeInfinity
    } else {
      min = ranges(0).toFloat
    }
    if (ranges(1).equalsIgnoreCase(POSITIVE_INFINITY_STRING)) {
      max = Float.PositiveInfinity
    } else if (ranges(1).equalsIgnoreCase(NEGATIVE_INFINITY_STRING)) {
      max = Float.NegativeInfinity
    } else {
      max = ranges(1).toFloat
    }
    AllowableRangeValues(min.toString, max.toString)
  }

  def readString(value: String, defaultValue: String = null, ignoreValue: String = null): String = {
    if (defaultValue != null && defaultValue.trim.length > 0) defaultValue
    else if (value == null) null
    else if (value.trim.length == 0) null
    else if (ignoreValue != null && value.equals(ignoreValue)) null
    else value.trim
  }

  def parseHttpMethod(method: Method, op: ApiOperation): String = {
    if (op.httpMethod() != null && op.httpMethod().trim().length() > 0) 
      op.httpMethod().trim
    else {
      if(method.getAnnotation(classOf[GET]) != null) "GET"
      else if(method.getAnnotation(classOf[DELETE]) != null) "DELETE"
      else if(method.getAnnotation(classOf[POST]) != null) "POST"
      else if(method.getAnnotation(classOf[PUT]) != null) "PUT"
      else if(method.getAnnotation(classOf[HEAD]) != null) "HEAD"
      else if(method.getAnnotation(classOf[OPTIONS]) != null) "OPTIONS"
      else null
    }
  }
}

class MutableParameter(param: Parameter) {
  var name: String = _
  var description: String = _
  var defaultValue: String = _
  var required: Boolean = _
  var allowMultiple: Boolean = false
  var dataType: String = _
  var allowableValues: AllowableValues = AnyAllowableValues
  var paramType: String = _
  var paramAccess: Option[String] = None

  if(param != null) {
    this.name = param.name
    this.description = param.description
    this.defaultValue = param.defaultValue
    this.required = param.required
    this.allowMultiple = param.allowMultiple
    this.dataType = param.dataType
    this.allowableValues = param.allowableValues
    this.paramType = param.paramType
    this.paramAccess = param.paramAccess
  }

  def this() = this(null)

  def asParameter() = {
    Parameter(name, 
      description, 
      defaultValue, 
      required, 
      allowMultiple, 
      dataType, 
      allowableValues, 
      paramType,
      paramAccess)
  }
}