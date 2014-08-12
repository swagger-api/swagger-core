package com.wordnik.swagger.jaxrs.listing

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs._

import javax.ws.rs._
import javax.ws.rs.core.MediaType

@Path("/api-docs")
@Api("/api-docs")
@Produces(Array("application/json; charset=utf-8", "application/json"))
class ApiListingResourceJSON extends ApiListingResource
