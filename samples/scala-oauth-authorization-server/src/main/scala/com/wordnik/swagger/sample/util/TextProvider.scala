package com.wordnik.swagger.sample.resource

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.sample.model._

import java.lang.annotation.Annotation

import javax.ws.rs.core.{ MultivaluedMap, MediaType }
import javax.ws.rs.ext.{ Provider, MessageBodyWriter }
import javax.ws.rs.Produces

import java.lang.reflect.Type
import java.io._

@Produces(Array(MediaType.TEXT_PLAIN))
@Provider
class TextProvider extends MessageBodyWriter[Pet] {
  override def isWriteable(`type`: Class[_], genericType: Type, annotations: Array[Annotation], mediaType: MediaType): Boolean = {
    classOf[Pet] == `type`
  }

  override def getSize(data: Pet, `type`: Class[_], genericType: Type, annotations: Array[Annotation], mediaType:  MediaType): Long = -1

  @throws(classOf[IOException])
  override def writeTo(data: Pet,
    `type`: Class[_], genericType: Type, 
    annotations: Array[Annotation],
    mediaType: MediaType, 
    headers: MultivaluedMap[String, AnyRef],
    out: OutputStream) = {
    out.write(data.toString.getBytes("utf-8"))
  }
}