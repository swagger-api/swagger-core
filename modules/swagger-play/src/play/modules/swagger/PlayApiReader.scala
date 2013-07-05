/**
 *  Copyright 2012 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package play.modules.swagger

import com.wordnik.swagger.core._
import com.wordnik.swagger.annotations._

import scala.collection.JavaConversions._
import org.slf4j.LoggerFactory

import java.lang.reflect.Method

import play.mvc.Router
import play.Logger
import play.classloading.enhancers.LocalvariablesNamesEnhancer

object PlayApiReader {
  private val endpointsCache = scala.collection.mutable.Map.empty[Class[_], Documentation]

  def read(hostClass: Class[_], apiVersion: String, swaggerVersion: String, basePath: String, apiPath: String): Documentation = {
    endpointsCache.get(hostClass) match {
      case None => val doc = new PlayApiSpecParser(hostClass, apiVersion, swaggerVersion, basePath, apiPath).parse; endpointsCache += hostClass -> doc.clone.asInstanceOf[Documentation]; doc
      case doc: Option[Documentation] => doc.get.clone.asInstanceOf[Documentation]
      case _ => null
    }
  }
}

/**
 * Reads swaggers annotations, play route information and uses reflection to build API information on a given class
 */
class PlayApiSpecParser(_hostClass: Class[_], _apiVersion: String, _swaggerVersion: String, _basePath: String, _resourcePath: String)
  extends ApiSpecParserTrait {
  private val LOGGER = LoggerFactory.getLogger(classOf[PlayApiSpecParser])

  override def hostClass = _hostClass
  override def apiVersion = _apiVersion
  override def swaggerVersion = _swaggerVersion
  override def basePath = _basePath
  override def resourcePath = _resourcePath

  // regular expression to extract content between {content}
  private val RX = """\{([^}]+)\}""".r
  private val POST = "post"

  LOGGER.debug(hostClass + ", apiVersion: " + apiVersion + ", swaggerVersion: " + swaggerVersion + ", basePath: " + basePath + ", resourcePath: " + resourcePath)

  val documentation = new Documentation
  val apiEndpoint = hostClass.getAnnotation(classOf[Api])

  override protected def processOperation(method: Method, o: DocumentationOperation) = {
    // set param names from method signatures
    // assuming natural order
    if (o.getParameters() != null && o.getParameters().length > 0) {
      val paramNames = LocalvariablesNamesEnhancer.lookupParameterNames(method).toList

      if (paramNames.length == o.getParameters().length) {
        var index = 0
        for (param <- o.getParameters()) {
          param.name = readString(paramNames.get(index), param.name)
          index = index + 1
        }
      }
    }
    
    // set http parameter type (query/path/body)
    getRoute(method) match {
      case Some(route) => {
        o.httpMethod = route.method

        if (o.getParameters() != null && o.getParameters().length > 0) {
          val routeHasPathParams = route.path.contains("{")

          if (routeHasPathParams) {
            val routePathParams: List[String] = (RX findAllIn route.path).toList

            for (param <- o.getParameters()) {
              param.paramType = routePathParams.find(_.equals("{" + param.name + "}")) match {
                case Some(p) => ApiValues.TYPE_PATH
                case None => if (POST.equalsIgnoreCase(route.method)) ApiValues.TYPE_BODY else ApiValues.TYPE_QUERY
              }
            }
          } else {
            for (param <- o.getParameters()) {
              param.paramType = if (POST.equalsIgnoreCase(route.method)) ApiValues.TYPE_BODY else ApiValues.TYPE_QUERY
            }
          }
        }
      }
      case None => Logger.info("Cannot process Operation. Nothing defined in play routes file for api method " + method.toString);
    }
    o
  }

  override def parseHttpMethod(method: Method, apiOperation: ApiOperation): String = {
    // does nothing, handled by getRoute
    ""
  }

  override def processParamAnnotations(docParam: DocumentationParameter,paramAnnotations: Array[java.lang.annotation.Annotation], method: Method): Boolean = {
    false
  }
  
  /**
   * Get the path which routes file points to for a given controller method
   */
  override def getPath(method: Method) = {
    getRoute(method) match {
      case Some(route) => route.path.replace(".json", ".{format}").replace(".xml", ".{format}")
      case None => Logger.info("Cannot determine Path. Nothing defined in play routes file for api method " + method.toString); this.resourcePath
    }
  }

  /**
   * Get the play route corresponding to a given java.lang.Method instance of a play controller method
   */
  private def getRoute(method: Method) = Router.routes.find((route) => {
    val parts = route.action.split("\\.")
    if (parts.length == 2) {
      val className = parts(0)
      val methodName = parts(1)

      val targetClassName = method.getDeclaringClass.getSimpleName
      val targetMethodName = method.getName

      // checking indexOf instead of equals to be compatible with scala where $ may be suffixed to controller class name
      targetClassName.indexOf(className) == 0 && targetMethodName.equals(methodName)
    } else {
      false
    }
  })
}
