package com.wordnik.swagger.core

/**
  * @author ayush
  * @since 6/23/11 12:49 PM
  *
  */


import scala.collection._
import mutable._
import scala.collection.JavaConversions._
import org.slf4j.LoggerFactory
import util.TypeUtil
import org.apache.commons.lang.StringUtils
import javax.ws.rs.core.{UriInfo, HttpHeaders}

class HelpApi {
  private val LOGGER = LoggerFactory.getLogger(classOf[HelpApi])

  var apiFilter : ApiAuthorizationFilter = null

  def this(apiFilterClassName: String) = {
    this()
    if(apiFilterClassName != null){
      try {
        apiFilter = Class.forName(apiFilterClassName).newInstance.asInstanceOf[ApiAuthorizationFilter]
      }
      catch {
        case e: ClassNotFoundException => LOGGER.error("Unable to resolve apiFilter class " + apiFilterClassName);
        case e: ClassCastException => LOGGER.error("Unable to cast to apiFilter class " + apiFilterClassName);
      }
    }
  }

  def filterDocs(doc: Documentation, headers: HttpHeaders, uriInfo: UriInfo, currentApiPath: String): Documentation = {
    //todo: apply auth and filter doc to only those which apply to current request/api-key
    if(apiFilter != null){
      var apisToRemove = new ListBuffer[DocumentationEndPoint]
      for (api <- JavaConversions.asIterator((doc.getApis).iterator())) {
        if( !apiFilter.authorize(api.path, headers, uriInfo) || api.path.equals(currentApiPath)) {
          //doc.removeApi( endPoint )
          apisToRemove + api
        }
      }
      for (api <- apisToRemove) doc.removeApi(api)
    }
    //todo: transform path?
    loadModel(doc)
    doc
  }

  private def loadModel(d: Documentation): Unit = {
    val directTypes = getReturnTypes(d)
    val types = TypeUtil.getReferencedClasses(directTypes)
    for (t <- types) {
      try {
        val clazz = Class.forName(t)
        val n = ApiPropertiesReader.read(clazz)
        if (null != n && null != n.getFields && n.getFields.length > 0){
          d.addModel(n)
          d.addSchema( n.getName, n.toDocumentationSchema() )
        }
      }
      catch {
        case e: ClassNotFoundException => LOGGER.error("Unable to resolve class " + t);
        case e: Exception => LOGGER.error("Unable to load model documentation for " + t, e)
      }
    }
  }

  private def getReturnTypes(d: Documentation): List[String] = {
    val l = new HashSet[String]
    if (d.getApis() != null) {
      //	endpoints
      for (n <- JavaConversions.asIterator((d.getApis()).iterator())) {
        //	operations
        for (o <- JavaConversions.asIterator((n.getOperations()).iterator())) {
          if (StringUtils.isNotBlank(o.getResponseTypeInternal())) l += o.getResponseTypeInternal().replaceAll("\\[\\]", "")
        }
      }
    }
    l.toList
  }

}