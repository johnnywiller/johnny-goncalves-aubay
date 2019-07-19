package com.sample.rest.server.controllers;

import com.sample.rest.server.repositories.LocationsRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
public class LocationsController {

    private final LocationsRepository locationsRepository;

    @Inject
    public LocationsController(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    @GET
    public List<String> index() {
        return locationsRepository.list();
    }

}
