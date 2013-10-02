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

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

@XmlRootElement(name = "User")
class User {
  @XmlElement(name = "id")
  @BeanProperty
  var id: Long = 0

  @XmlElement(name = "username")
  @BeanProperty
  var username: String = _

  @XmlElement(name = "firstName")
  @BeanProperty
  var firstName: String = _

  @XmlElement(name = "lastName")
  @BeanProperty
  var lastName: String = _

  @XmlElement(name = "email")
  @BeanProperty
  var email: String = _

  @XmlElement(name = "password")
  @BeanProperty
  var password: String = _

  @XmlElement(name = "phone")
  @BeanProperty
  var phone: String = _

  @XmlElement(name = "userStatus")
  @ApiProperty(value = "User Status", allowableValues = "1-registered,2-active,3-closed")
  @BeanProperty
  var userStatus: Int = _
}