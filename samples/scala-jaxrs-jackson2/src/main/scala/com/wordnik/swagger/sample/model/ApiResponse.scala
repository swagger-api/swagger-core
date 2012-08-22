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

package com.wordnik.swagger.sample.model

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

object ApiResponse {
  val ERROR = 1
  val WARNING = 2
  val INFO = 3
  val OK = 4
  val TOO_BUSY = 5
}

class ApiResponse(@BeanProperty var code: Int, @BeanProperty var message: String) {
  def this() = this(ApiResponse.ERROR, null)
  def getType(): String = code match {
    case ApiResponse.ERROR => "error"
    case ApiResponse.WARNING => "warning"
    case ApiResponse.INFO => "info"
    case ApiResponse.OK => "ok"
    case ApiResponse.TOO_BUSY => "too busy"
    case _ => "unknown"
  }
  def setType(`type`: String) = {}
}
