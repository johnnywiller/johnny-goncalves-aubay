package com.sample.rest.server.controllers;

import com.sample.rest.server.application.Configuration;
import com.sample.rest.server.controllers.transferobjects.BookingCommand;
import com.sample.rest.server.core.TimeProvider;
import com.sample.rest.server.domain.BookStatus;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.time.format.DateTimeFormatter;

import static javax.ws.rs.core.Response.Status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class BookingControllerTest extends JerseyTest {

    private final String SUCCESS_MSG = BookStatus.SUCCESS.getMessage();
    private final String FAILURE_MSG = BookStatus.FAILURE.getMessage();
    private String currentDate;

    @Override
    public Application configure() {
        return new Configuration().registerClasses(BookingController.class);
    }

    @Before
    public void setCurrentDate() {
        currentDate = new TimeProvider()
            .getCurrentDateTime()
            .format(DateTimeFormatter
                .ofPattern(TimeProvider.DATE_FORMAT));
    }

    @Test
    public void shouldFailNoSpaceAvailable() {
        final int INVALID_SPACE = Integer.MAX_VALUE;
        int experienceId = 9890;
        BookingCommand bookingCommand = new BookingCommand(experienceId, this.currentDate, INVALID_SPACE);

        Response response = target("/booking")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(bookingCommand));

        assertThat(response.getStatusInfo(), is(Status.OK));

        try (JsonReader reader = Json
            .createReader(new StringReader(response.readEntity(String.class)))) {
            JsonObject failedBooking = reader.readObject();
            assertThat(failedBooking.getString("status"), is(BookStatus.FAILURE.toString()));
            assertThat(failedBooking.getString("message"), is(FAILURE_MSG));
        }
    }

    @Test
    public void shouldFailNoExperience() {
        final int INVALID_EXPERIENCE = -9999;
        BookingCommand bookingCommand = new BookingCommand(INVALID_EXPERIENCE, this.currentDate, 1);

        Response response = target("/booking")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(bookingCommand));

        assertThat(response.getStatusInfo(), is(equalTo(Status.BAD_REQUEST)));

        try (JsonReader reader = Json
            .createReader(new StringReader(response.readEntity(String.class)))) {
            JsonObject failedBooking = reader.readObject();
            assertThat(failedBooking.getString("msg"), is("Experience doesn't exist"));
        }
    }

    @Test
    public void shouldBookForOnePerson() {
        int experienceId = 9890;
        BookingCommand bookingCommand = new BookingCommand(experienceId, this.currentDate, 1);

        Response response = target("/booking")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(bookingCommand));

        assertThat(response.getStatusInfo(), is(equalTo(Status.OK)));

        try (JsonReader reader = Json
            .createReader(new StringReader(response.readEntity(String.class)))) {
            JsonObject bookedExperience = reader.readObject();
            assertThat(bookedExperience.getString("status"), is(BookStatus.SUCCESS.toString()));
            assertThat(bookedExperience.getInt("code"), is(9890));
            assertThat(bookedExperience.getString("message"), is(SUCCESS_MSG));
            assertThat(bookedExperience.getJsonNumber("price").toString(), is("267.00"));
            assertThat(bookedExperience.getInt("travelers"), is(1));
        }
    }
}