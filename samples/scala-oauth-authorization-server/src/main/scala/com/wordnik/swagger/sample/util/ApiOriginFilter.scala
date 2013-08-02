/**
 *  Copyright 2013 Wordnik, Inc.
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

package com.wordnik.swagger.sample.util

import java.io.IOException

import javax.servlet._
import javax.servlet.http.HttpServletResponse

class ApiOriginFilter extends javax.servlet.Filter {
  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) = {
    val res = response.asInstanceOf[HttpServletResponse]
    res.addHeader("Access-Control-Allow-Origin:", "*")
    res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
    res.addHeader("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");
    chain.doFilter(request, response)
  }

  override def destroy() = {}

  @throws(classOf[ServletException])
  override def init(filterConfig: FilterConfig) {}
}