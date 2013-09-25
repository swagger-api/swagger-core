/**
 * Copyright 2012 Wordnik, Inc.
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

import com.wordnik.swagger.core.{SwaggerSpec, SwaggerContext}
import play.api.{Logger, Application, Plugin}
import play.api.Play._
import com.wordnik.swagger.config.{SwaggerConfig, ConfigFactory, ScannerFactory}
import com.wordnik.swagger.reader.ClassReaders

class SwaggerPlugin(application: Application) extends Plugin {

  override def onStart() {
    Logger("swagger").info("Plugin - starting initialisation")

    val apiVersion = current.configuration.getString("api.version") match {
      case None => "beta"
      case Some(value) => value
    }
    val basePath = current.configuration.getString("swagger.api.basepath") match {
      case None => "http://localhost"
      case Some(value) => value
    }

    ConfigFactory.setConfig(new SwaggerConfig(apiVersion, SwaggerSpec.version, basePath, ""))
    SwaggerContext.registerClassLoader(current.classloader)
    ScannerFactory.setScanner(new PlayApiScanner(current.routes))
    ClassReaders.reader = Some(new PlayApiReader(current.routes, current.configuration))
    val docRoot = ""
    ApiListingCache.listing(docRoot)

    Logger("swagger").info("Plugin - initialisation done")
  }

  override def onStop() {
    // we may need to tidy up resources here
    Logger("swagger").info("Plugin - stopped");
  }
}