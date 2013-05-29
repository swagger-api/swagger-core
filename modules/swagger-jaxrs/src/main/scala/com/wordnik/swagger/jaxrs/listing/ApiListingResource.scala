package com.wordnik.swagger.jaxrs.listing

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs._

import javax.ws.rs._

@Path("/api-docs")
@Api("/api-docs")
// @Produces(Array("application/json"))
class ApiListingResourceJSON extends ApiListing
