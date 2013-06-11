package com.wordnik.swagger.jaxrs

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter.ModelConverters
import com.wordnik.swagger.config._
import com.wordnik.swagger.reader.ClassReader
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

trait JaxrsApiReader extends ClassReader {
  private val LOGGER = LoggerFactory.getLogger(classOf[JaxrsApiReader])

  // decorates a Parameter based on annotations, returns None if param should be ignored
  def processParamAnnotations(mutable: MutableParameter, paramAnnotations: Array[Annotation], method: Method): Option[Parameter]

  def parseOperation(method: Method, apiOperation: ApiOperation, errorResponses: List[ErrorResponse], isDeprecated: String) = {
    val api = method.getAnnotation(classOf[Api])
    val responseClass = apiOperation.responseContainer match {
      case "" => apiOperation.response.getName
      case e: String => "%s[%s]".format(e, apiOperation.response.getName)
    }

    val paramAnnotations = method.getParameterAnnotations
    val paramTypes = method.getParameterTypes
    val genericParamTypes = method.getGenericParameterTypes
    val produces = apiOperation.produces match {
      case e: String if(e != "") => e.split(",").map(_.trim).toList
      case _ => List()
    }
    val consumes = apiOperation.consumes match {
      case e: String if(e != "") => e.split(",").map(_.trim).toList
      case _ => List()
    }
    val protocols = apiOperation.protocols match {
      case e: String if(e != "") => e.split(",").map(_.trim).toList
      case _ => List()
    }
    val authorizations = apiOperation.authorizations match {
      case e: String if(e != "") => e.split(",").map(_.trim).toList
      case _ => List()
    }
    val params = (for((annotations, paramType, genericParamType) <- (paramAnnotations, paramTypes, genericParamTypes).zipped.toList) yield {
      if(annotations.length > 0) {
        val param = new MutableParameter
        param.dataType = ModelConverters.toName(paramType)
        processParamAnnotations(param, annotations, method)
      }
      else if(paramTypes.size > 0) {
        //  it's a body param w/o annotations, which means POST.  Only take the first one!
        val p = paramTypes.head

        val param = new MutableParameter
        param.dataType = ModelConverters.toName(p)
        param.name = TYPE_BODY
        param.paramType = TYPE_BODY

        Some(param.asParameter)
      }
      else None
    }).flatten.toList

    Operation(
      parseHttpMethod(method, apiOperation),
      apiOperation.value,
      apiOperation.notes,
      responseClass,
      method.getName,
      apiOperation.position,
      produces,
      consumes,
      protocols,
      authorizations,
      params,
      errorResponses,
      Option(isDeprecated))
  }

  def readMethod(method: Method) = {
    val apiOperation = method.getAnnotation(classOf[ApiOperation])
    val errorAnnotations = method.getAnnotation(classOf[ApiErrors])

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

    parseOperation(method, apiOperation, errorResponses, isDeprecated)
  }

  def appendOperation(endpoint: String, path: String, op: Operation, operations: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]]) = {
    operations.filter(op => op._1 == endpoint) match {
      case e: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]] if(e.size > 0) => e.head._3 += op
      case _ => operations += Tuple3(endpoint, path, new ListBuffer[Operation]() ++= List(op))
    }
  }

  def read(docRoot: String, cls: Class[_], config: SwaggerConfig): Option[ApiListing] = {
    val api = cls.getAnnotation(classOf[Api])
    if(api != null) {
      val operations = new ListBuffer[Tuple3[String, String, ListBuffer[Operation]]]()
      val description = api.description match {
        case e: String if(e != "") => Some(e)
        case _ => None
      }

      for(method <- cls.getMethods) {
        val returnType = method.getReturnType
        val path = method.getAnnotation(classOf[Path]) match {
          case e: Path => e.value()
          case _ => ""
        }
        Option(returnType.getAnnotation(classOf[Api])) match {
          case Some(e) => {
            // the return class has Api annotations, and should be parsed
            for(submethod <- returnType.getMethods) {
              if(submethod.getAnnotation(classOf[ApiOperation]) != null) {
                val op = readMethod(submethod)
                val endpoint = api.value + pathFromMethod(method)
                val subpath = submethod.getAnnotation(classOf[Path]) match {
                  case e: Path => e.value()
                  case _ => ""
                }
                appendOperation(endpoint, path + subpath, op, operations)
              }
            }
          }
          case _ => {
            if(method.getAnnotation(classOf[ApiOperation]) != null) {
              val op = readMethod(method)
              val endpoint = api.value + pathFromMethod(method)
              appendOperation(endpoint, path, op, operations)
            }            
          }
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

      val models = ModelUtil.modelsFromApis(apis)
      Some(ApiListing (
        apiVersion = config.apiVersion,
        swaggerVersion = config.swaggerVersion,
        basePath = config.basePath,
        resourcePath = (docRoot + api.value),
        apis = ModelUtil.stripPackages(apis),
        models = models,
        description = description)
      )
    }
    else None
  }

  def pathFromMethod(method: Method): String = {
    val path = method.getAnnotation(classOf[javax.ws.rs.Path])
    if(path == null) ""
    else path.value
  }

  def parseApiParamAnnotation(param: MutableParameter, annotation: ApiParam, method: Method) {
    param.name = readString(annotation.name, param.name)
    param.description = Option(readString(annotation.value))
    param.defaultValue = Option(readString(annotation.defaultValue))

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
  var description: Option[String] = None
  var defaultValue: Option[String] = None
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