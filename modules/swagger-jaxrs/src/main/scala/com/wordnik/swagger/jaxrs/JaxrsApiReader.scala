package com.wordnik.swagger.jaxrs

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.config._
import com.wordnik.swagger.reader.{ ClassReader, ClassReaderUtils }
import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.model._

import org.slf4j.LoggerFactory

import java.lang.reflect.{ Method, Type, Field }
import java.lang.annotation.Annotation

import javax.ws.rs._
import javax.ws.rs.core.Context

import scala.collection.JavaConverters._
import scala.collection.mutable.{ ListBuffer, HashMap, HashSet }

trait JaxrsApiReader extends ClassReader with ClassReaderUtils {
  private val LOGGER = LoggerFactory.getLogger(classOf[JaxrsApiReader])
  val GenericTypeMapper = "([a-zA-Z\\.]*)<([a-zA-Z0-9\\.\\,\\s]*)>".r

  // decorates a Parameter based on annotations, returns None if param should be ignored
  def processParamAnnotations(mutable: MutableParameter, paramAnnotations: Array[Annotation]): Option[Parameter]

  def processDataType(paramType: Class[_], genericParamType: Type) = {
    paramType.getName match {
      case "[I" => "Array[int]"
      case "[Z" => "Array[boolean]"
      case "[D" => "Array[double]"
      case "[F" => "Array[float]"
      case "[J" => "Array[long]"
      case _ => {
        if(paramType.isArray) {
          "Array[%s]".format(paramType.getComponentType.getName)
        }
        else {
          genericParamType.toString match {
            case GenericTypeMapper(container, base) => {
              val qt = SwaggerTypes(base.split("\\.").last) match {
                case "object" => base
                case e: String => e
              }
              val b = ModelUtil.modelFromString(qt) match {
                case Some(e) => e._2.qualifiedType
                case None => qt
              }
              "%s[%s]".format(normalizeContainer(container), b)
            }
            case _ => paramType.getName
          }
        }
      }
    }
  }

  def normalizeContainer(str: String) = {
    if(str.indexOf(".List") >= 0) "List"
    else if(str.indexOf(".Set") >= 0) "Set"
    else {
      println("UNKNOWN TYPE: " + str)
      "UNKNOWN"
    }
  }

  def parseOperation(
    method: Method, 
    apiOperation: ApiOperation, 
    apiResponses: List[ResponseMessage],
    isDeprecated: String,
    parentParams: List[Parameter],
    parentMethods: ListBuffer[Method]
  ) = {
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

    val produces = Option(apiOperation.produces) match {
      case Some(e) if(e != "") => e.split(",").map(_.trim).toList
      case _ => method.getAnnotation(classOf[Produces]) match {
        case e: Produces => e.value.toList
        case _ => List()
      }
    }
    val consumes = Option(apiOperation.consumes) match {
      case Some(e) if(e != "") => e.split(",").map(_.trim).toList
      case _ => method.getAnnotation(classOf[Consumes]) match {
        case e: Consumes => e.value.toList
        case _ => List()
      }
    }
    val protocols = Option(apiOperation.protocols) match {
      case Some(e) if(e != "") => e.split(",").map(_.trim).toList
      case _ => List()
    }
    val authorizations = Option(apiOperation.authorizations) match {
      case Some(e) if(e != "") => e.split(",").map(_.trim).toList
      case _ => List()
    }
    val params = parentParams ++ (for((annotations, paramType, genericParamType) <- (paramAnnotations, paramTypes, genericParamTypes).zipped.toList) yield {
      if(annotations.length > 0) {
        val param = new MutableParameter
        param.dataType = processDataType(paramType, genericParamType)
        processParamAnnotations(param, annotations)
      }
      else if(paramTypes.size > 0) {
        //  it's a body param w/o annotations, which means POST.  Only take the first one!
        val p = paramTypes.head

        val param = new MutableParameter
        param.dataType = p.getName
        param.name = TYPE_BODY
        param.paramType = TYPE_BODY

        Some(param.asParameter)
      }
      else None
    }).flatten.toList

    val implicitParams = {
      val returnType = method.getReturnType
      LOGGER.debug("checking for implicits")
      Option(method.getAnnotation(classOf[ApiImplicitParams])) match {
        case Some(e) => {
          (for(param <- e.value) yield {
            LOGGER.debug("processing " + param)
            val allowableValues = toAllowableValues(param.allowableValues)
            Parameter(
              param.name,
              Option(readString(param.value)),
              Option(param.defaultValue).filter(_.trim.nonEmpty),
              param.required,
              param.allowMultiple,
              param.dataType,
              allowableValues,
              param.paramType,
              Option(param.access).filter(_.trim.nonEmpty))
          }).toList
        }
        case _ => List()
      }
    }

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
      params ++ implicitParams,
      apiResponses,
      Option(isDeprecated))
  }

  def readMethod(method: Method, parentParams: List[Parameter], parentMethods: ListBuffer[Method]) = {
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

    parseOperation(method, apiOperation, apiResponses, isDeprecated, parentParams, parentMethods)
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

  def readRecursive(
    docRoot: String, 
    parentPath: String, cls: Class[_], 
    config: SwaggerConfig,
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
      // look for method-level annotated properties
      val parentParams: List[Parameter] = (for(field <- getAllFields(cls)) 
        yield {
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
            processParamAnnotations(param, annotations)
          }
          else None
        }
      ).flatten.toList

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
              val op = readMethod(method, parentParams, parentMethods)
              appendOperation(endpoint, path, op, operations)
            }
          }
        }
      }
      // sort them by min position in the operations
      val s = (for(op <- operations) yield {
        (op, op._3.map(_.position).toList.min)
      }).sortWith(_._2 < _._2).toList
      val orderedOperations = new ListBuffer[Tuple3[String, String, ListBuffer[Operation]]]
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

  def getAllFields(cls: Class[_]): List[Field] = {
    var fields = cls.getDeclaredFields().toList;                 
    if (cls.getSuperclass() != null) {
        fields = getAllFields(cls.getSuperclass()) ++ fields;
    }   
    return fields;
  }
  
  def pathFromMethod(method: Method): String = {
    val path = method.getAnnotation(classOf[javax.ws.rs.Path])
    if(path == null) ""
    else path.value
  }

  def parseApiParamAnnotation(param: MutableParameter, annotation: ApiParam) {
    param.name = readString(annotation.name, param.name)
    param.description = Option(readString(annotation.value))
    param.defaultValue = Option(readString(annotation.defaultValue))

    try {
      param.allowableValues = toAllowableValues(annotation.allowableValues)
    } catch {
      case e: Exception =>
        LOGGER.error("Allowable values annotation problem in method for parameter " + param.name)
    }
    param.required = annotation.required
    param.allowMultiple = annotation.allowMultiple
    param.paramAccess = Option(readString(annotation.access))
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

  def addLeadingSlash(e: String): String = {
    e.startsWith("/") match {
      case true => e
      case false => "/" + e
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