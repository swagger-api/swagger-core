package com.wordnik.swagger.oauth

import com.wordnik.swagger.auth.service.ValidatorFactory

import javax.servlet.http.HttpServlet

class Bootstrap extends HttpServlet {
  val validator = new SampleValidator

  ValidatorFactory.validator = validator
}
