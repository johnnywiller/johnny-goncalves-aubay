package com.sample.rest.server.controllers;

import com.sample.rest.server.core.TimeProvider;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.TreeMap;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthController {

    private final TimeProvider timeProvider;

    @Inject
    public HealthController(final TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @GET
    @Path("/check")
    public Map<String, String> check() {
        final Map<String, String> healthResult = new TreeMap<>();
        healthResult.put("className", getClass().getCanonicalName());
        healthResult.put("checkTime", String.valueOf(timeProvider.getTimeInMillis()));
        return healthResult;
    }
}

