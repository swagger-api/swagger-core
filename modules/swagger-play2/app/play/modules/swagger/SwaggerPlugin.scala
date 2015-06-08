/**
 * Copyright 2014 Reverb Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package play.modules.swagger

import javax.inject.Inject

import com.wordnik.swagger.core.{SwaggerSpec, SwaggerContext}
import play.api.routing.Router
import play.api.{Logger, Application, Plugin}
import play.api.Play._
import com.wordnik.swagger.config.{FilterFactory, SwaggerConfig, ConfigFactory, ScannerFactory}
import com.wordnik.swagger.reader.ClassReaders
import com.wordnik.swagger.core.filter.SwaggerSpecFilter

class SwaggerPlugin @Inject() (application: Application, router: Router) extends Plugin {

  override def onStart() {
    Logger("swagger").info("Plugin - starting initialisation")

    val apiVersion = current.configuration.getString("api.version") match {
      case None => "beta"
      case Some(value) => value
    }

    val basePath = current.configuration.getString("swagger.api.basepath") match {
      case Some(e) if (e != "") => {
        //ensure basepath is a valid URL, else throw an exception
        try {
          val basepathUrl = new java.net.URL(e)
          Logger("swagger").info("Basepath configured as: %s".format(e))
          e
        } catch {
          case ex: Exception =>
            Logger("swagger").error("Misconfiguration - basepath not a valid URL: %s. Swagger plugin abandoning initialisation".format(e))
            throw ex
        }
      }
      case _ => "http://localhost"
    }

    SwaggerContext.registerClassLoader(current.classloader)
    ConfigFactory.config.apiVersion = apiVersion
    ConfigFactory.config.basePath = basePath
    ScannerFactory.setScanner(new PlayApiScanner(router))
    ClassReaders.reader = Some(new PlayApiReader(router))

    current.configuration.getString("swagger.filter") match {
      case Some(e) if (e != "") => {
        try {
          FilterFactory.filter = SwaggerContext.loadClass(e).newInstance.asInstanceOf[SwaggerSpecFilter]
          Logger("swagger").info("Setting swagger.filter to %s".format(e))
        }
        catch {
          case ex: Exception => Logger("swagger").error("Failed to load filter " + e, ex)
        }
      }
      case _ =>
    }

    val docRoot = ""
    ApiListingCache.listing(docRoot)

    Logger("swagger").info("Plugin - initialization done")
  }

  override def onStop() {
    ApiListingCache.cache = None
    Logger("swagger").info("Plugin - stopped");
  }
}