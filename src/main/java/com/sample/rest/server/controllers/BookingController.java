package com.sample.rest.server.controllers;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/booking")
@Produces(MediaType.APPLICATION_JSON)
public class BookingController {

    @POST
    public String index() {
        return null;
    }

}
