package com.wordnik.swagger.sample.service

import com.wordnik.swagger.sample.model._

import scala.collection.mutable._

object Cache {
  val codeCache = new HashSet[String]
  val tokenCache = new HashMap[String, TokenWrapper]
}