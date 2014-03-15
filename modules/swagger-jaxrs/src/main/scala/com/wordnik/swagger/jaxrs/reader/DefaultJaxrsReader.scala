package com.wordnik.swagger.jaxrs.reader

import com.wordnik.swagger.model._
import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.config._
import com.wordnik.swagger.core.util.ModelUtil
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.ApiValues._

import java.lang.reflect.{ Method, Type }
import java.lang.annotation.Annotation

import javax.ws.rs._
import javax.ws.rs.core.Context

import scala.collection.mutable.{ ListBuffer, HashMap, HashSet }

class DefaultJaxrsApiReader extends JaxrsApiReader {
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
        val returnType = findSubresourceType(method)
        val path = method.getAnnotation(classOf[Path]) match {
          case e: Path => e.value()
          case _ => ""
        }
        val endpoint = (parentPath + pathFromMethod(method)).replace("//", "/")
        Option(returnType.getAnnotation(classOf[Api])) match {
          case Some(e) => {
            val root = docRoot + api.value + pathFromMethod(method)
            parentMethods += method
            readRecursive(root, endpoint, returnType, config, operations, parentMethods)
            parentMethods -= method
          }
          case _ => {
            if(method.getAnnotation(classOf[ApiOperation]) != null) {
              readMethod(method, parentParams, parentMethods) match {
                case Some(op) => appendOperation(endpoint, path, op, operations)
                case None =>
              }
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

  // decorates a Parameter based on annotations, returns None if param should be ignored
  def processParamAnnotations(mutable: MutableParameter, paramAnnotations: Array[Annotation]): Option[Parameter] = {
    var shouldIgnore = false
    for (pa <- paramAnnotations) {
      pa match {
        case e: ApiParam => parseApiParamAnnotation(mutable, e)
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
        case e: DefaultValue => {
          mutable.defaultValue = Option(readString(e.value))
        }
        case e: Context => shouldIgnore = true
        case _ =>
      }
    }
    if(!shouldIgnore) {
      if(mutable.paramType == null) {
        mutable.paramType = TYPE_BODY
        mutable.name = TYPE_BODY
      }
      Some(mutable.asParameter)
    }
    else None
  }

  def findSubresourceType(method: Method): Class[_] = {
    method.getReturnType
  }
}
