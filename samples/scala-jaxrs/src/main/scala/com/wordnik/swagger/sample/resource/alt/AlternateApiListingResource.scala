package com.wordnik.swagger.sample.resource.alt

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.jaxrs._

import javax.ws.rs._

@Path("/api-docs.json")
@Api("/api-docs")
@Produces(Array("application/json"))
class ApiListingResourceJSON extends AlternateApiListing

@Path("/api-docs.xml")
@Api("/api-docs")
@Produces(Array("application/xml"))
class ApiListingResourceXML extends AlternateApiListing