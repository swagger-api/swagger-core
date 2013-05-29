package com.wordnik.swagger.jaxrs.listing

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._

import java.lang.annotation.Annotation

import javax.ws.rs.core.{ MultivaluedMap, MediaType }
import javax.ws.rs.ext.{ Provider, MessageBodyWriter }
import javax.ws.rs.Produces

import java.lang.reflect.Type
import java.io._

@Produces(Array(MediaType.APPLICATION_JSON))
@Provider
class ApiListingProvider extends MessageBodyWriter[com.wordnik.swagger.model.ApiListing] {
  override def isWriteable(`type`: Class[_], genericType: Type, annotations: Array[Annotation], mediaType: MediaType): Boolean = {
    classOf[com.wordnik.swagger.model.ApiListing].isAssignableFrom(`type`)
  }

  override def getSize(data: com.wordnik.swagger.model.ApiListing, `type`: Class[_], genericType: Type, annotations: Array[Annotation], mediaType:  MediaType): Long = -1

  @throws(classOf[IOException])
  override def writeTo(data: com.wordnik.swagger.model.ApiListing,
    `type`: Class[_], genericType: Type, 
    annotations: Array[Annotation],
    mediaType: MediaType, 
    headers: MultivaluedMap[String, AnyRef],
    out: OutputStream) = {
    out.write(JsonSerializer.asJson(data).getBytes())
  }
}

@Produces(Array(MediaType.APPLICATION_JSON))
@Provider
class ResourceListingProvider extends MessageBodyWriter[ResourceListing] {
  override def isWriteable(`type`: Class[_], genericType: Type, annotations: Array[Annotation], mediaType: MediaType): Boolean = {
    classOf[ResourceListing].isAssignableFrom(`type`)
  }

  override def getSize(data: ResourceListing, `type`: Class[_], genericType: Type, annotations: Array[Annotation], mediaType:  MediaType): Long = -1

  @throws(classOf[IOException])
  override def writeTo(data: ResourceListing,
    `type`: Class[_], genericType: Type, 
    annotations: Array[Annotation],
    mediaType: MediaType, 
    headers: MultivaluedMap[String, AnyRef],
    out: OutputStream) = {
    out.write(JsonSerializer.asJson(data).getBytes())
  }
}
