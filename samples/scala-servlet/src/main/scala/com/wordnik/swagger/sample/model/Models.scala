package com.wordnik.swagger.sample.model

case class SampleData (id: Long, name: String, email: String, age: Int, dateOfBirth: java.util.Date)
case class ApiResponse (code: Int, message: String)