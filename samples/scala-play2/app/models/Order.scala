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

package models

import com.wordnik.swagger.annotations._

import java.util.Date
import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

@XmlRootElement(name = "Order")
class Order {
  @XmlElement(name = "id")
  @BeanProperty
  var id: Long = 0

  @XmlElement(name = "petId")
  @BeanProperty
  var petId: Long = 0

  @XmlElement(name = "quantity")
  @BeanProperty
  var quantity: Int = 0

  @XmlElement(name = "shipDate")
  @BeanProperty
  var shipDate: Date = null

  @XmlElement(name = "status")
  @ApiProperty(value = "Order Status", allowableValues = "placed,approved,delivered")
  @BeanProperty
  var status: String = null
}