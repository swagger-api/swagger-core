package play.modules.swagger

import com.wordnik.swagger.core._
import com.wordnik.swagger.play._

import org.codehaus.jackson.map.ObjectMapper

import javax.xml.bind.JAXBContext
import java.io.StringWriter

import play.api.Play.current
import play.api.Logger

import scala.collection.mutable.ListBuffer
import scala.Predef._
import scala.collection.JavaConversions._

/**
  * Exposes two primay methods: to get a list of available resources and to get details on a given resource
  *
  * @author ayush
  * @since 10/9/11 4:05 PM
  *
  */
object ApiHelpInventory {
  // Add the Play classloader to Swagger
  SwaggerContext.registerClassLoader(current.classloader)

  // Get a list of all controller classes
  private val controllerClasses = ListBuffer.empty[Class[_]]

  // Initialize the map from Api annotation value to play controller class
  private val resourceMap = scala.collection.mutable.Map.empty[String, Class[_]]

  // Read various configurable properties. These can be specified in application.conf
  private val apiVersion = current.configuration.getString("api.version") match {case None => "beta" case Some(value) => value}
  private val basePath =   current.configuration.getString("swagger.api.basepath") match {case None => "http://localhost" case Some(value) => value}
  private val swaggerVersion = SwaggerSpec.version
  private val apiFilterClassName = current.configuration.getString("swagger.security.filter") match {case None => null case Some(value) => value} 

  private val filterOutTopLevelApi = true

  def getResourceNames: java.util.List[String] = getResourceMap.keys.toList

  private val jaxbContext = JAXBContext.newInstance(classOf[Documentation]);
  private val jacksonObjectMapper = new ObjectMapper();

  /**
    * Get a list of all top level resources
    */
  private def getRootResources(format: String) = {
    var apiFilter: ApiAuthorizationFilter = null
    if(null != apiFilterClassName) {
      try {
        apiFilter = SwaggerContext.loadClass(apiFilterClassName).newInstance.asInstanceOf[ApiAuthorizationFilter]
      }
      catch {
        case e: ClassNotFoundException => Logger.error("Unable to resolve apiFilter class " + apiFilterClassName);
        case e: ClassCastException => Logger.error("Unable to cast to apiFilter class " + apiFilterClassName);
      }
    }

    val allApiDoc = new Documentation
    for (clazz <- getControllerClasses) {
      val apiAnnotation = clazz.getAnnotation(classOf[Api])
      if(null != apiAnnotation){
        val api = new DocumentationEndPoint(apiAnnotation.value + ".{format}", apiAnnotation.description())
        if(!isApiAdded(allApiDoc, api)) {
          if (null == apiFilter || apiFilter.authorizeResource(api.path, null, null)){
            allApiDoc.addApi(api)
          }
        }
      }
    }

    allApiDoc.swaggerVersion = swaggerVersion
    allApiDoc.basePath = basePath
    allApiDoc.apiVersion = apiVersion

    allApiDoc
  }

  /**
    * Get detailed API/models for a given resource
    */
  private def getResource(resourceName: String) = {
    getResourceMap.get(resourceName) match {
      case Some(clazz) => {
        val currentApiEndPoint = clazz.getAnnotation(classOf[Api])
        val currentApiPath = if (currentApiEndPoint != null && filterOutTopLevelApi) currentApiEndPoint.value else null

	    Logger.debug("Loading resource " + resourceName + " from " + clazz + " @ " + currentApiPath)

        val docs = new HelpApi(apiFilterClassName).filterDocs(
          PlayApiReader.read(clazz, apiVersion, swaggerVersion, basePath, currentApiPath), null, null, currentApiPath, null)

        Option(docs)
      }

      case None => None
    }

  }

  def getPathHelpJson(apiPath: String): String = {
    getResource(apiPath) match {
      case Some(docs) => jacksonObjectMapper.writeValueAsString(docs)
      case None => null
    }
  }

  def getPathHelpXml(apiPath: String): String = {
    getResource(apiPath) match {
      case Some(docs) => {
        val stringWriter = new StringWriter()
        jaxbContext.createMarshaller().marshal(docs, stringWriter);
        stringWriter.toString
      }
      case None => null
    }
  }

  def getRootHelpJson(): String = {
    jacksonObjectMapper.writeValueAsString(getRootResources("json"))
  }

  def getRootHelpXml(): String = {
      val stringWriter = new StringWriter()
      jaxbContext.createMarshaller().marshal(getRootResources("xml"), stringWriter);
      stringWriter.toString
  }


  /**
    * Get a list of all controller classes in Play
    */
  private def getControllerClasses = {
    if(this.controllerClasses.length == 0) {
	  val swaggerControllers = current.getTypesAnnotatedWith("controllers", classOf[Api])
 
      if(swaggerControllers.size() > 0) {
        for (clazzName <- swaggerControllers) {
	        val clazz = current.classloader.loadClass(clazzName)
			this.controllerClasses += clazz;
            val apiAnnotation = clazz.getAnnotation(classOf[Api])
			if(apiAnnotation != null && classOf[play.api.mvc.Controller].isAssignableFrom(clazz)) {
	            Logger.debug("Found Resource " + apiAnnotation.value + " @ " + clazzName)
	            resourceMap += apiAnnotation.value -> clazz
			}

        }

      }

    }
   
    controllerClasses
  }

  private def getResourceMap = {
    // check if resources and controller info has already been loaded
    if(controllerClasses.length == 0) {
      this.getControllerClasses;
    }

    this.resourceMap;
  }

  private def isApiAdded(allApiDoc: Documentation, endpoint: DocumentationEndPoint): Boolean = {
    var isAdded: Boolean = false
    if (allApiDoc.getApis != null) {
      for (addedApi <- allApiDoc.getApis()) {
        if (endpoint.path.equals(addedApi.path)) isAdded = true
      }
    }
    isAdded
  }
}