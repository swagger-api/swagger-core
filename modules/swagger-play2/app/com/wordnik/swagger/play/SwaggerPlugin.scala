package com.wordnik.swagger.play

import play.Plugin
import play.api.Application
import play.api.Logger

class SwaggerPlugin(val app: Application) extends Plugin {
  override def onStart() {
	Logger.info("SwaggerPlugin.start")
  }

  override def onStop() {
	Logger.info("SwaggerPlugin.stop")
  }

  override def enabled() = !app.configuration.getString("swagger").filter(_ == "disabled").isDefined
}