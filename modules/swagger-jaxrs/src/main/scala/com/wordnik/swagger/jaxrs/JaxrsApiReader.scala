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

  def parseOperation(method: Method, apiOperation: ApiOperation, apiResponses: List[ResponseMessage],
                     isDeprecated: String, parentMethods: ListBuffer[Method]) = {
    val api = method.getAnnotation(classOf[Api])
    val responseClass = {
      val baseName = apiOperation.response.getName
      val output = apiOperation.responseContainer match {
        case "" => baseName
        case e: String => "%s[%s]".format(e, baseName)
      }
      output
    }

    var paramAnnotations: Array[Array[java.lang.annotation.Annotation]] = null
    var paramTypes: Array[java.lang.Class[_]] = null
    var genericParamTypes: Array[java.lang.reflect.Type] = null

    if (parentMethods.isEmpty) {
      paramAnnotations = method.getParameterAnnotations
      paramTypes = method.getParameterTypes
      genericParamTypes = method.getGenericParameterTypes
    } else {
      paramAnnotations = parentMethods.map(pm => pm.getParameterAnnotations).reduceRight(_ ++ _) ++ method.getParameterAnnotations
      paramTypes = parentMethods.map(pm => pm.getParameterTypes).reduceRight(_ ++ _) ++ method.getParameterTypes
      genericParamTypes = parentMethods.map(pm => pm.getGenericParameterTypes).reduceRight(_ ++ _) ++ method.getGenericParameterTypes
    }

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
        LOGGER.debug("looking up dataType for " + paramType)
        param.dataType = paramType.getName //ModelConverters.toName(paramType)
        processParamAnnotations(param, annotations, method)
      }
      else if(paramTypes.size > 0) {
        //  it's a body param w/o annotations, which means POST.  Only take the first one!
        val p = paramTypes.head

        val param = new MutableParameter
        param.dataType = p.getName //ModelConverters.toName(p)
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
      apiResponses,
      Option(isDeprecated))
  }

  def readMethod(method: Method, parentMethods: ListBuffer[Method]) = {
    val apiOperation = method.getAnnotation(classOf[ApiOperation])
    val responseAnnotation = method.getAnnotation(classOf[ApiResponses])
    val apiResponses = {
      if(responseAnnotation == null) List()
      else (for(response <- responseAnnotation.value) yield {
        val apiResponseClass = {
          if(response.response != classOf[Void])
            Some(response.response.getName)
          else None
        }
        ResponseMessage(response.code, response.message, apiResponseClass)}
      ).toList
    }
    val isDeprecated = Option(method.getAnnotation(classOf[Deprecated])).map(m => "true").getOrElse(null)

    parseOperation(method, apiOperation, apiResponses, isDeprecated, parentMethods)
  }

  def appendOperation(endpoint: String, path: String, op: Operation, operations: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]]) = {
    operations.filter(op => op._1 == endpoint) match {
      case e: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]] if(e.size > 0) => e.head._3 += op
      case _ => operations += Tuple3(endpoint, path, new ListBuffer[Operation]() ++= List(op))
    }
  }

  def read(docRoot: String, cls: Class[_], config: SwaggerConfig): Option[ApiListing] = {
    readRecursive(docRoot, "", cls, config, new ListBuffer[Tuple3[String, String, ListBuffer[Operation]]], new ListBuffer[Method])
  }

  def readRecursive(docRoot: String, parentPath: String, cls: Class[_], config: SwaggerConfig,
           operations: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]],
           parentMethods: ListBuffer[Method]): Option[ApiListing] = {
    val api = cls.getAnnotation(classOf[Api])

    // must have @Api annotation to process!
    if(api != null) {
      val consumes = Option(api.consumes) match {
        case Some(e) if(e != "") => e.split(",").map(_.trim).toList
        case _ => cls.getAnnotation(classOf[Consumes]) match {
          case e: Consumes => e.value.toList
          case _ => List()
        }
      }
      val produces = Option(api.produces) match {
        case Some(e) if(e != "") => e.split(",").map(_.trim).toList
        case _ => cls.getAnnotation(classOf[Produces]) match {
          case e: Produces => e.value.toList
          case _ => List()
        }
      }
      val protocols = Option(api.protocols) match {
        case Some(e) if(e != "") => e.split(",").map(_.trim).toList
        case _ => List()
      }
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
        val endpoint = parentPath + api.value + pathFromMethod(method)
        Option(returnType.getAnnotation(classOf[Api])) match {
          case Some(e) => {
            val root = docRoot + api.value + pathFromMethod(method)
            parentMethods += method
            readRecursive(root, endpoint, returnType, config, operations, parentMethods)
          }
          case _ => {
            if(method.getAnnotation(classOf[ApiOperation]) != null) {
              val op = readMethod(method, parentMethods)
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
        description = description,
        produces = produces,
        consumes = consumes,
        protocols = protocols)
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
      else if(method.getAnnotation(classOf[PATCH]) != null) "PATCH"
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