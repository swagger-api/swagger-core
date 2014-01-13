package com.wordnik.swagger.sample.resource

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.sample.model._

import java.lang.annotation.Annotation

import javax.xml.bind._
import javax.xml.bind.annotation._
import java.io.ByteArrayOutputStream

import javax.ws.rs.core.{ MultivaluedMap, MediaType }
import javax.ws.rs.ext.{ Provider, MessageBodyWriter }
import javax.ws.rs.Produces

import java.lang.reflect.Type
import java.io._

@Produces(Array(MediaType.TEXT_HTML))
@Provider
class HtmlProvider extends MessageBodyWriter[Pet] {
  override def isWriteable(`type`: Class[_], genericType: Type, annotations: Array[Annotation], mediaType: MediaType): Boolean = {
    classOf[Pet] == `type`
  }

  override def getSize(data: Pet, `type`: Class[_], genericType: Type, annotations: Array[Annotation], mediaType:  MediaType): Long = -1

  @throws(classOf[IOException])
  override def writeTo(pet: Pet,
    `type`: Class[_], genericType: Type, 
    annotations: Array[Annotation],
    mediaType: MediaType, 
    headers: MultivaluedMap[String, AnyRef],
    out: OutputStream) = {
    try{
      val html = 
        <html>
          <body>
            <div>Pet: {pet.name} ({pet.id})</div>
          </body>
        </html>
      out.write(html.toString.getBytes("utf-8"))
    }
    catch {
      case e: Exception => {
        e.printStackTrace
        throw e
      }
    }
  }
}