package com.sample.rest.server.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchController {

    @GET
    public String index() {
        return null;
    }

}
