package com.sample.rest.server.controllers;

import com.sample.rest.server.controllers.transferobjects.SearchResult;
import com.sample.rest.server.core.TimeProvider;
import com.sample.rest.server.service.ExperienceService;

import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Objects;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchController {

    private ExperienceService service;

    @Inject
    public SearchController(ExperienceService service) {
        this.service = service;
    }

    @GET
    public Response searchExperiences(
        @QueryParam("location") String location,
        @QueryParam("travelers") Integer travelers,
        @QueryParam("date") String date) throws BadRequestException {

        if (!ensureParametersValidity(location, travelers, date))
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder()
                    .add("msg", "Some parameters are empty")
                    .build()).build();

        LocalDate travelDate;

        try {
            travelDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(TimeProvider.DATE_FORMAT));
        } catch (DateTimeParseException e) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder()
                    .add("msg", "Invalid date")
                    .build()).build();
        }

        HashMap<String, Object> search = getParametersAsMap(location, travelers, date);

        return Response.ok(new SearchResult(search, service
            .getExperiences(travelDate, travelers, location))).build();
    }

    private HashMap<String, Object> getParametersAsMap(String location, Integer travelers, String date) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("location", location);
        map.put("travelers", travelers);
        map.put("date", date);
        return map;
    }

    private boolean ensureParametersValidity(String location, Integer travelers, String date) {
        return Objects.nonNull(location) &&
            Objects.nonNull(travelers) &&
            Objects.nonNull(date) &&
            !location.isBlank();
    }
}
