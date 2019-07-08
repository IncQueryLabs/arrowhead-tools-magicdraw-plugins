package com.incquerylabs.onetoonetest.arrowheadevent;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import eu.arrowhead.client.common.model.Event;

@Path("notify")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ArrowheadEventRecRest {
	
	 @GET
	  public Response getIt() {
	    return Response.ok().build();
	  }

	  @POST
	  public Response receiveEvent(Event event) {
	    return Response.ok().build();
	  }
}
