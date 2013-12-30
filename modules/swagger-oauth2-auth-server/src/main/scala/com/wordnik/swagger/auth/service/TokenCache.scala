package com.wordnik.swagger.auth.service

import com.wordnik.swagger.auth.model.TokenWrapper

import scala.collection.mutable.{ HashSet, HashMap }

object TokenCache {
  val codeCache = new HashSet[String]
  val tokenCache = new HashMap[String, TokenWrapper]  
}

trait TokenCache {
  def codeCache = TokenCache.codeCache
  def tokenCache = TokenCache.tokenCache
}
