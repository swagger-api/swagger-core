package com.wordnik.swagger.servlet.config

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter.ModelConverters
import com.wordnik.swagger.config._
import com.wordnik.swagger.reader.{ ClassReader, ClassReaderUtils }
import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.core.ApiValues._
import com.wordnik.swagger.model._

import org.slf4j.LoggerFactory

import java.lang.reflect.{ Method, Type }
import java.lang.annotation.Annotation

import scala.collection.JavaConverters._
import scala.collection.mutable.{ ListBuffer, HashMap, HashSet }

class ServletReader extends ClassReader with ClassReaderUtils {
  private val LOGGER = LoggerFactory.getLogger(classOf[ServletReader])
  val ComplexTypeMatcher = "([a-zA-Z]*)\\[([a-zA-Z\\.\\-]*)\\].*".r

  def read(docRoot: String, cls: Class[_], config: SwaggerConfig): Option[ApiListing] = {
    val api = cls.getAnnotation(classOf[Api])
    if(api != null) {
      val fullPath = {
        if(api.value.startsWith("/")) api.value.substring(1)
        else api.value
      }
      val (resourcePath, subpath) = {
        if(fullPath.indexOf("/") > 0) {
          val pos = fullPath.indexOf("/")
          ("/" + fullPath.substring(0, pos), fullPath.substring(pos))
        }
        else ("/", fullPath)
      }
      LOGGER.debug("read routes from classes: %s, %s".format(resourcePath, subpath))
      val operations = new ListBuffer[Operation]
      for(method <- cls.getMethods) {
        // only process mehods with @ApiOperation annotations
        if(method.getAnnotation(classOf[ApiOperation]) != null) {
          // process only @ApiImplicitParams
          val parameters = {
            val paramListAnnotation = method.getAnnotation(classOf[ApiImplicitParams])
            if(paramListAnnotation != null) {
              (for(param <- paramListAnnotation.value) yield {
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
          val produces = opa.produces match {
            case e: String if(e != "") => e.split(",").map(_.trim).toList
            case _ => List()
          }
          val consumes = opa.consumes match {
            case e: String if(e != "") => e.split(",").map(_.trim).toList
            case _ => List()
          }
          val protocols = opa.protocols match {
            case e: String if(e != "") => e.split(",").map(_.trim).toList
            case _ => List()
          }
          val authorizations:List[com.wordnik.swagger.model.Authorization] = Option(opa.authorizations) match {
            case Some(e) => (for(a <- e) yield {
              val scopes = (for(s <- a.scopes) yield com.wordnik.swagger.model.AuthorizationScope(s.scope, s.description)).toArray
              new com.wordnik.swagger.model.Authorization(a.value, scopes)
            }).toList
            case _ => List()
          }
          val responseClass = opa.responseContainer match {
            case "" => opa.response.getName
            case e: String => "%s[%s]".format(e, opa.response.getName)
          }
          val responseAnnotations = method.getAnnotation(classOf[ApiResponses])
          val apiResponses = {
            if(responseAnnotations == null) List()
            else (for(response <- responseAnnotations.value) yield {
              val apiResponse = {
                if(response.response != classOf[Void])
                  Some(response.response.getName)
                else None
              }
              ResponseMessage(response.code, response.message, apiResponse)}
            ).toList
          }

          operations += Operation(
            opa.httpMethod,
            opa.value,
            opa.notes,
            responseClass,
            opa.nickname,
            opa.position,
            produces, // produces
            consumes, // consumes
            protocols, // protocols
            authorizations, // authorizations
            parameters, // params
            apiResponses, // errors
            None)
        }
      }

      if(operations.size > 0) {
        val apis = List(
          ApiDescription(
            "/" + fullPath,
            Some("description"),
            operations.toList))
        val models = ModelUtil.modelsFromApis(apis)
        Some(
          ApiListing (
            config.apiVersion,
            SwaggerSpec.version,
            config.basePath,
            resourcePath,
            List(), // produces
            List(), // consumes
            List(), // protocols
            List(), // authorizations
            ModelUtil.stripPackages(apis),
            models)
        )
      }
      else None
    }
    else None
  }
}