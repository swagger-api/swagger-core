package com.wordnik.swagger.sample.api 

import org.scalatra.swagger.{ JacksonSwaggerBase, Swagger }
import org.scalatra.ScalatraServlet
import org.json4s._


class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase {
  // no format param for this api
  override val includeFormatParameter = false
	
  private[this] def removeNulls(initial: JValue): JValue = {
    initial match {
      case JArray(values) ⇒ JArray(values map removeNulls)
      case j: JObject ⇒ JObject(j.obj map {
        case JField(nm, JNull) ⇒ JField(nm, JNothing)
        case JField(nm, jo)    ⇒ JField(nm, removeNulls(jo))
      })
      case _ ⇒ initial
    }
  }

  override protected def renderDoc(doc: ApiType): JValue = removeNulls(Extraction.decompose(doc))
}
