package com.wordnik.swagger.jaxrs

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.config._
import com.wordnik.swagger.reader.{ ClassReader, ClassReaderUtils }
import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.model._

import org.slf4j.LoggerFactory

import java.lang.reflect.{ Method, Type, Field, ParameterizedType }
import java.lang.annotation.Annotation

import javax.ws.rs._
import javax.ws.rs.core.Context

import scala.collection.JavaConverters._
import scala.collection.mutable.{ ListBuffer, HashMap, HashSet }

trait JaxrsApiReader extends ClassReader with ClassReaderUtils {
  private val LOGGER = LoggerFactory.getLogger(classOf[JaxrsApiReader])
  val GenericTypeMapper = "([a-zA-Z\\.]*)<([a-zA-Z0-9\\.\\,\\s]*)>".r

  // decorates a Parameter based on annotations, returns None if param should be ignored
  def processParamAnnotations(mutable: MutableParameter, paramAnnotations: Array[Annotation]): List[Parameter]

  // Finds the type of the subresource this method produces, in case it's a subresource locator
  // In case it's not a subresource locator the entity type is returned
  def findSubresourceType(method: Method): Class[_]

  def readRecursive(
    docRoot: String, 
    parentPath: String, cls: Class[_], 
    config: SwaggerConfig,
    operations: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]],
    parentMethods: ListBuffer[Method]): Option[ApiListing]

  def processDataType(paramType: Class[_], genericParamType: Type) = {
    paramType.getName match {
      case "[I" => "Array[int]"
      case "[Z" => "Array[boolean]"
      case "[D" => "Array[double]"
      case "[F" => "Array[float]"
      case "[J" => "Array[long]"
      case _ => {
        if(paramType.isArray) {
          if (paramType.getComponentType.isEnum) {
            "Array[string]"
          }
          else {
            "Array[%s]".format(paramType.getComponentType.getName)
          }
        }
        else if(paramType.isEnum) {
          "string"
        }
        else {
          genericParamType.toString match {
            case GenericTypeMapper(container, base) => {
              val baseType = genericParamType.asInstanceOf[ParameterizedType].getActualTypeArguments()(0).asInstanceOf[Class[_]]
              val qt = if (baseType.isEnum) "string" else SwaggerTypes(base.split("\\.").last) match {
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

  def processAllowableValues(paramType: Class[_], genericParamType: Type) = {
    if(paramType.isEnum) {
      AllowableListValues(paramType.asInstanceOf[Class[Enum[_]]].getEnumConstants.map(_.name).toList)
    }
    else if (paramType.isArray && paramType.getComponentType.isEnum) {
      AllowableListValues(paramType.getComponentType.asInstanceOf[Class[Enum[_]]].getEnumConstants.map(_.name).toList)
    }
    else {
      AnyAllowableValues
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
      if(apiOperation != null){
        val baseName = processDataType(apiOperation.response, apiOperation.response)
        val output = apiOperation.responseContainer match {
          case "" => baseName
          case e: String => "%s[%s]".format(e, baseName)
        }
        output
      }
      else {
        if(!"javax.ws.rs.core.Response".equals(method.getReturnType.getCanonicalName))
          processDataType(method.getReturnType, method.getGenericReturnType)
        else
          "void"
      }
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

    val (nickname, produces, consumes, protocols, authorizations) = {
      if(apiOperation != null) {
        (
        (if(apiOperation.nickname != null && apiOperation.nickname != "") 
          apiOperation.nickname
        else
          method.getName
        ),
        Option(apiOperation.produces) match {
          case Some(e) if(e != "") => e.split(",").map(_.trim).toList
          case _ => method.getAnnotation(classOf[Produces]) match {
            case e: Produces => e.value.toList
            case _ => List()
          }
        },
        Option(apiOperation.consumes) match {
          case Some(e) if(e != "") => e.split(",").map(_.trim).toList
          case _ => method.getAnnotation(classOf[Consumes]) match {
            case e: Consumes => e.value.toList
            case _ => List()
          }
        },
        Option(apiOperation.protocols) match {
          case Some(e) if(e != "") => e.split(",").map(_.trim).toList
          case _ => List()
        },
        Option(apiOperation.authorizations) match {
          case Some(e) => (for(a <- e) yield {
            val scopes = (for(s <- a.scopes) yield com.wordnik.swagger.model.AuthorizationScope(s.scope, s.description)).toArray
            new com.wordnik.swagger.model.Authorization(a.value, scopes)
          }).toList
          case _ => List()
        })
      }
      else(method.getName, List(), List(), List(), List())
    }
    val params = parentParams ++ (for((annotations, paramType, genericParamType) <- (paramAnnotations, paramTypes, genericParamTypes).zipped.toList) yield {
      if(annotations.length > 0) {
        val param = new MutableParameter
        param.dataType = processDataType(paramType, genericParamType)
        param.allowableValues = processAllowableValues(paramType, genericParamType)
        processParamAnnotations(param, annotations)
      }
      else /* If it doesn't have annotations, it must be a body parameter, and it's safe to assume that there will only
              ever be one of these in the sequence according to JSR-339 JAX-RS 2.0 section 3.3.2.1. */
      {
        val param = new MutableParameter
        param.dataType = processDataType(paramType, genericParamType)
        param.allowableValues = processAllowableValues(paramType, genericParamType)
        param.name = TYPE_BODY
        param.paramType = TYPE_BODY

        List(param.asParameter)
      }
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
              name = param.name,
              description = Option(readString(param.value)),
              defaultValue = Option(param.defaultValue).filter(_.trim.nonEmpty),
              required = param.required,
              allowMultiple = param.allowMultiple,
              dataType = param.dataType,
              allowableValues = allowableValues,
              paramType = param.paramType,
              paramAccess = Option(param.access).filter(_.trim.nonEmpty))
          }).toList
        }
        case _ => List()
      }
    }

    val (summary, notes, position) = {
      if(apiOperation != null) (apiOperation.value, apiOperation.notes, apiOperation.position)
      else ("","",0)
    }

    Operation(
      method = parseHttpMethod(method, apiOperation),
      summary = summary,
      notes = notes,
      responseClass = responseClass,
      nickname = nickname,
      position = position,
      produces = produces,
      consumes = consumes,
      protocols = protocols,
      authorizations = authorizations,
      parameters = params ++ implicitParams,
      responseMessages = apiResponses,
      `deprecated` = Option(isDeprecated))
  }

  def readMethod(method: Method, parentParams: List[Parameter], parentMethods: ListBuffer[Method]): Option[Operation] = {
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

    val hidden = if(apiOperation != null)
      apiOperation.hidden
    else false

    if(!hidden) Some(parseOperation(method, apiOperation, apiResponses, isDeprecated, parentParams, parentMethods))
    else None
  }

  def appendOperation(endpoint: String, path: String, op: Operation, operations: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]]) = {
    operations.filter(op => op._1 == endpoint) match {
      case e: ListBuffer[Tuple3[String, String, ListBuffer[Operation]]] if(e.size > 0) => e.head._3 += op
      case _ => operations += Tuple3(endpoint, path, new ListBuffer[Operation]() ++= List(op))
    }
  }

  def read(docRoot: String, cls: Class[_], config: SwaggerConfig): Option[ApiListing] = {
    val parentPath = {
      Option(cls.getAnnotation(classOf[Path])) match {
        case Some(e) => e.value()
        case _ => ""
      }
    }
    readRecursive(docRoot, parentPath.replace("//","/"), cls, config, new ListBuffer[Tuple3[String, String, ListBuffer[Operation]]], new ListBuffer[Method])
  }

  def getAllFields(cls: Class[_]): List[Field] = {
    var fields = cls.getDeclaredFields().toList;                 
    if (cls.getSuperclass() != null) {
        fields = getAllFields(cls.getSuperclass()) ++ fields;
    }   
    return fields;
  }

  def getAllParamsFromFields(cls: Class[_]): List[Parameter] = {
    // find getter/setters
    (cls.getDeclaredMethods map {
      method =>
        if (method.getAnnotation(classOf[QueryParam]) != null ||
          method.getAnnotation(classOf[HeaderParam]) != null ||
          method.getAnnotation(classOf[PathParam]) != null ||
          method.getAnnotation(classOf[ApiParam]) != null) {
          createParameterFromGetterOrSetter(method).toList
        } else Nil
    }).flatten.toList ++
      (for (field <- getAllFields(cls)) yield {
        // only process fields with @ApiParam, @QueryParam, @HeaderParam, @PathParam
        if (field.getAnnotation(classOf[QueryParam]) != null ||
          field.getAnnotation(classOf[HeaderParam]) != null ||
          field.getAnnotation(classOf[PathParam]) != null ||
          field.getAnnotation(classOf[ApiParam]) != null) {
          val param = new MutableParameter
          param.dataType = processDataType(field.getType, field.getGenericType)
          param.allowableValues = processAllowableValues(field.getType, field.getGenericType)
          Option(field.getAnnotation(classOf[ApiParam])) match {
            case Some(annotation) => toAllowableValues(annotation.allowableValues)
            case _ =>
          }
          val annotations = field.getAnnotations
          processParamAnnotations(param, annotations)
        }
        else Nil
      }).flatten.toList
  }
  
  private val getterPattern = """get(.+)""".r
  private val setterPattern = """set(.+)""".r
  
  private def createParameterFromGetterOrSetter(method: Method): List[Parameter] = {
    (method.getName match {
      case getterPattern(propertyName) =>
        val param = new MutableParameter
        // TODO: not sure this will work
        param.dataType = processDataType(method.getReturnType, method.getGenericReturnType)
        param.allowableValues = processAllowableValues(method.getReturnType, method.getGenericReturnType)
        Some(param)
      case setterPattern(propertyName) =>
        val param = new MutableParameter
        // TODO: not sure this will work
        param.dataType = processDataType(method.getParameterTypes()(0), method.getGenericParameterTypes()(0))
        param.allowableValues = processAllowableValues(method.getParameterTypes()(0), method.getGenericParameterTypes()(0))
        Some(param)
      case _ => None
    }).toList.map {
      param =>
        Option(method.getAnnotation(classOf[ApiParam])) match {
          case Some(annotation) => toAllowableValues(annotation.allowableValues)
          case _ =>
        }
        val annotations = method.getAnnotations
        processParamAnnotations(param, annotations)
    }.flatten
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
      if (annotation.allowableValues != null && !annotation.allowableValues.isEmpty) {
        param.allowableValues = toAllowableValues(annotation.allowableValues)
      }
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
    if (op != null && op.httpMethod() != null && op.httpMethod().trim().length() > 0)
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
