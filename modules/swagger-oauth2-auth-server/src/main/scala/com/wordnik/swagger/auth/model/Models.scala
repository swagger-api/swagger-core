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

package com.wordnik.swagger.auth.model

import java.util.Date

case class ApiResponseMessage(code: Int, message: String)

trait TokenResponse {
  def expires_in: Long
  def access_token: String
}
case class AnonymousTokenResponse(expires_in: Long, access_token: String) extends TokenResponse
case class UserTokenResponse(expires_in: Long, access_token: String, userId: Long) extends TokenResponse

case class TokenWrapper(createdAt: Date, tokenResponse: TokenResponse) {
  def getRemaining() = {
    val ageInMillis = (tokenResponse.expires_in * 1000 - age) match {
      case i: Long if(i < 0) => 0
      case i: Long => i
    }
    (ageInMillis / 1000.0).toLong
  }

  def age = System.currentTimeMillis - createdAt.getTime
}