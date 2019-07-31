package com.sample.rest.server.controllers;

import com.sample.rest.server.controllers.transferobjects.BookedExperienceResult;
import com.sample.rest.server.controllers.transferobjects.BookingCommand;
import com.sample.rest.server.core.TimeProvider;
import com.sample.rest.server.domain.BookingException;
import com.sample.rest.server.domain.valueobject.BookExperienceVO;
import com.sample.rest.server.domain.valueobject.BookedExperienceVO;
import com.sample.rest.server.repositories.BookingRepository;
import com.sample.rest.server.service.ExperienceService;

import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/booking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {

    private ExperienceService experienceService;
    private BookingRepository bookingRepository;

    @Inject
    public BookingController(ExperienceService experienceService, BookingRepository bookingRepository) {
        this.experienceService = experienceService;
        this.bookingRepository = bookingRepository;
    }

    @GET
    public List<BookedExperienceVO> list() {
        return bookingRepository.findAll();
    }

    @POST
    public Response bookExperience(BookingCommand bookingCommand) {
        LocalDate travelDate;
        try {
            travelDate = LocalDate.parse(bookingCommand.getDate(), DateTimeFormatter.ofPattern(TimeProvider.DATE_FORMAT));
        } catch (DateTimeParseException e) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder()
                    .add("msg", "Invalid date")
                    .build()).build();
        }

        BookExperienceVO bookExperienceVO = BookExperienceVO.of(
            bookingCommand.getCode(),
            travelDate,
            bookingCommand.getTravelers()
        );

        BookedExperienceVO bookedExperienceVO;
        try {
            bookedExperienceVO = experienceService.bookExperience(bookExperienceVO);
        } catch (BookingException e) {
            e.printStackTrace();
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder()
                    .add("msg", e.getMessage())
                    .build()).build();
        }
        BookedExperienceResult bookedExperienceResult = BookedExperienceResult
            .fromBookedExperienceVO(bookedExperienceVO);

        return Response.ok(bookedExperienceResult).build();
    }
}
