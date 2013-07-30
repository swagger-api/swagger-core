package com.wordnik.swagger.sample.model

case class User(firstName: String, lastName: String, email: String)
case class ApiResponseMessage(code: Int, message: String)