package com.wordnik.swagger.sample.resources

import com.wordnik.swagger.annotations.*

import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path('/api/api-docs.json')
@Api("/api-docs")
@Produces("application/json")
class ApiListing extends com.wordnik.swagger.jaxrs.listing.ApiListing {}