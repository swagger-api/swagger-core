package com.wordnik.swagger.sample.api 

import org.scalatra.swagger.{ JacksonSwaggerBase, Swagger }
import org.scalatra.ScalatraServlet
import org.json4s._


class ResourceListing(implicit val swagger: Swagger) 
	extends ScalatraServlet 
	with JacksonSwaggerBase {
  // no .{format} param for this api!
  override val includeFormatParameter = false
}
