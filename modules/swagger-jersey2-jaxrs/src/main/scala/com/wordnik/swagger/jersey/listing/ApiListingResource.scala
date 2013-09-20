package com.wordnik.swagger.jersey.listing

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jersey._

import javax.ws.rs._
import javax.ws.rs.core.MediaType

@Path("/api-docs")
@Api("/api-docs")
@Produces(Array(MediaType.APPLICATION_JSON))
class ApiListingResourceJSON extends ApiListingResource