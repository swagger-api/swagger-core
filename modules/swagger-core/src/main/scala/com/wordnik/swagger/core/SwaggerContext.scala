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

package com.wordnik.swagger.core

import collection.mutable.ListBuffer
import org.slf4j.{ LoggerFactory, Logger }

object SwaggerContext {
  private val LOGGER = LoggerFactory.getLogger("com.wordnik.swagger.core.SwaggerContext")

  var suffixResponseFormat = true

  private val classLoaders = ListBuffer.empty[ClassLoader]
  registerClassLoader(this.getClass.getClassLoader)

  def registerClassLoader(cl: ClassLoader) = this.classLoaders += cl

  def loadClass(name: String) = {
    var clazz: Class[_] = null

    for (classLoader <- classLoaders.reverse) {
      if (clazz == null) {
        try {
          clazz = Class.forName(name, true, classLoader)
        } catch {
          case e: ClassNotFoundException => LOGGER.debug("Class not found in classLoader " + classLoader)
        }
      }
    }

    if (clazz == null)
      throw new ClassNotFoundException("class " + name + " not found")
    clazz
  }
}