package com.wordnik.swagger.sample.resource;

import javax.ws.rs.*;

@Path("/resources")
@Api("/resources")
@Produces({"application/json","application/xml"})
public class ApiListingResource extends JavaApiListing {}
