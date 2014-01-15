package com.wordnik.swagger.auth.service

import com.wordnik.swagger.auth.model.ApiResponseMessage

trait AuthDialog {
  def show(clientId: String, redirectUri: String, scope: String): ApiResponseMessage
}