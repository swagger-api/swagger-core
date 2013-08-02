package com.wordnik.swagger.jersey.listing

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jersey._

import javax.ws.rs._

@Path("/api-docs.json")
@Api("/api-docs")
@Produces(Array("application/json"))
class ApiListingResourceJSON extends ApiListing

@Path("/api-docs.xml")
@Api("/api-docs")
@Produces(Array("application/xml"))
class ApiListingResourceXML extends ApiListing