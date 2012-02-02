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

package com.wordnik.swagger.sample.resource

import com.wordnik.swagger.sample.exception.{ ApiException, BadRequestException, NotFoundException }
import com.wordnik.swagger.sample.model.ApiResponse

import javax.ws.rs.ext.{ ExceptionMapper, Provider }
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status

/**
 * Date: 8/1/11
 * Time: 3:27 PM
 */
@Provider
class SampleExceptionMapper extends ExceptionMapper[ApiException] {
  def toResponse(exception: ApiException): Response = {
    exception match {
      case e: NotFoundException => Response.status(Status.NOT_FOUND).entity(new ApiResponse(ApiResponse.ERROR, e.getMessage())).build
      case e: BadRequestException => Response.status(Status.BAD_REQUEST).entity(new ApiResponse(ApiResponse.ERROR, e.getMessage())).build
      case e: ApiException => Response.status(Status.BAD_REQUEST).entity(new ApiResponse(ApiResponse.ERROR, e.getMessage())).build
      case _ => Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ApiResponse(ApiResponse.ERROR, "a system error occured")).build
    }
  }
}

@Provider
class SystemExceptionMapper extends ExceptionMapper[RuntimeException] {
  def toResponse(exception: RuntimeException): Response = {
    exception match {
      case e: com.sun.jersey.api.NotFoundException => Response.status(Status.NOT_FOUND).entity(new ApiResponse(ApiResponse.ERROR, e.getMessage())).build
      case e: javax.ws.rs.WebApplicationException => {
        Response.status(e.getResponse.getStatus).entity(new ApiResponse(e.getResponse.getStatus, e.getMessage())).build
        e.printStackTrace
        e.getResponse
      }
      case e => {
        e.printStackTrace
        throw e
      }
    }
  }
}