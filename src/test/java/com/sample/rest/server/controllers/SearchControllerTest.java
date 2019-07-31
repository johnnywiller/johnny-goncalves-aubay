package com.sample.rest.server.controllers;

import com.sample.rest.server.application.Configuration;
import com.sample.rest.server.core.TimeProvider;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.json.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static javax.ws.rs.core.Response.Status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SearchControllerTest extends JerseyTest {

    String currentDate;

    @Override
    public Application configure() {
        return new Configuration().registerClasses(SearchController.class);
    }

    @Before
    public void setup() {
        this.currentDate = new TimeProvider()
            .getCurrentDateTime()
            .toLocalDate()
            .format(DateTimeFormatter
                .ofPattern(TimeProvider.DATE_FORMAT));
    }

    @Test
    public void shouldErrorInsufficientParameters() {
        Response response = target("/search")
            .request()
            .get();

        try (JsonReader reader = Json
            .createReader(new StringReader(response.readEntity(String.class)))) {
            JsonObject msg = reader.readObject();
            assertThat(response.getStatusInfo(), is(Status.BAD_REQUEST));
            assertThat(msg.getString("msg"), is("Some parameters are empty"));
        }
    }

    @Test
    public void shouldErrorInsufficientParameters2() {
        Response response = target("/search")
            .queryParam("travelers")
            .queryParam("location")
            .queryParam("date")
            .request()
            .get();

        try (JsonReader reader = Json
            .createReader(new StringReader(response.readEntity(String.class)))) {
            JsonObject msg = reader.readObject();
            assertThat(response.getStatusInfo(), is(Status.BAD_REQUEST));
            assertThat(msg.getString("msg"), is("Some parameters are empty"));
        }
    }

    @Test
    public void shouldErrorInvalidDate() {
        String invalidDate = "20190132";
        Response response = target("/search")
            .queryParam("travelers", 1)
            .queryParam("location", "LONDON")
            .queryParam("date", invalidDate) //some invalid date
            .request()
            .get();

        try (JsonReader reader = Json
            .createReader(new StringReader(response.readEntity(String.class)))) {
            JsonObject msg = reader.readObject();
            assertThat(response.getStatusInfo(), is(Status.BAD_REQUEST));
            assertThat(msg.getString("msg"), is("Invalid date"));
        }
    }

    @Test
    public void shouldFindByTravelers() {
        int travelers = 5;
        String location = "PARIS";
        String travelDate = LocalDate.now().format(DateTimeFormatter.ofPattern(TimeProvider.DATE_FORMAT));

        Response response = target("/search")
            .queryParam("travelers", travelers)
            .queryParam("location", location)
            .queryParam("date", travelDate)
            .request()
            .get();

        assertThat(response.getStatusInfo(), is(equalTo(Status.OK)));

        try (JsonReader reader = Json
            .createReader(new StringReader(response.readEntity(String.class)))) {

            JsonArray experiences = reader
                .readObject()
                .getJsonObject("results")
                .getJsonArray("items");

            assertThat(experiences, is(not(empty())));

            experiences.getValuesAs(JsonValue::asJsonObject)
                .forEach(experience ->
                    assertThat(experience.getInt("availability"),
                        is(greaterThanOrEqualTo(travelers))));
        }
    }

    @Test
    public void shouldFindFiveLondonExperience() {
        String location = "LONDON";
        int experiencesSize = 5;
        int travelers = 10;

        Response response = target("/search")
            .queryParam("location", location)
            .queryParam("travelers", travelers)
            .queryParam("date", this.currentDate)
            .request()
            .get();

        assertThat(response.getStatusInfo(), is(Status.OK));

        try (JsonReader reader = Json
            .createReader(new StringReader(response.readEntity(String.class)))) {

            Integer matching = reader
                .readObject()
                .getJsonObject("results")
                .getInt("matching");

            assertThat(matching, is(experiencesSize));
        }
    }
}
