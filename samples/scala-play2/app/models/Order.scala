/**
 *  Copyright 2014 Reverb Technologies, Inc.
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

package models

import com.wordnik.swagger.annotations._

import java.util.Date

import scala.annotation.meta.field

@ApiModel("Order")
case class Order (
  @(ApiModelProperty @field)(position=1)id: Long,
  @(ApiModelProperty @field)(position=2)petId: Long,
  @(ApiModelProperty @field)(position=3)quantity: Int,
  @(ApiModelProperty @field)(position=4)shipDate: Date,
  @(ApiModelProperty @field)(position=5)status: String
)