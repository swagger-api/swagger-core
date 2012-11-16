package com.wordnik.swagger.sample.resource

import com.wordnik.util.perf._
import com.wordnik.resource.util._

import com.wordnik.swagger.core._
import com.wordnik.swagger.core.util.RestResourceUtil
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs._

import com.wordnik.swagger.sample.model.User
import com.wordnik.swagger.sample.data.UserData
import com.wordnik.swagger.sample.exception.NotFoundException

import javax.ws.rs.core.Response
import javax.ws.rs._

trait HealthResource extends ProfileEndpointTrait

@Path("/health.json")
@Api(value="/health" , description = "Health information about the server")
@Produces(Array("application/json"))
class HealthResourceJSON extends HealthResource

@Path("/health.xml")
@Api(value="/health", description = "Health information about the server")
@Produces(Array("application/xml"))
class HealthResourceXML extends HealthResource