package com.wordnik.swagger.core

import javax.ws.rs.{Produces, Path}

@Path("/resources.json")
@Api("/resources")
@Produces(Array("application/json"))
class ApiListingResourceJSON extends ApiListing

@Path("/reources.xml")
@Api("/resources")
@Produces(Array("application/xml"))
class ApiListingResourceXML extends ApiListing
