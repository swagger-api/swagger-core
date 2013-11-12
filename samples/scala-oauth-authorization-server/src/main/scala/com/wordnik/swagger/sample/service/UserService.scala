package com.wordnik.swagger.sample.service

import com.wordnik.swagger.auth.service.TokenScope
import com.wordnik.swagger.sample.model._

object UserService {
  val users = Map("1" -> User("Tony", "Tam", "fehguy@gmail.com"))

  def getUser(): Option[User] = {
    TokenScope.getUserId() match {
      case e: java.lang.Long => Option(users.getOrElse(e.toString, null))
      case _ => Option(User("Anonymous", "Bob", "anon@anonymous.comxxx"))
    }
  }
}
