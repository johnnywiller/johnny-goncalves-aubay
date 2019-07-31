package com.sample.rest.server.repositories;

import com.sample.rest.server.domain.Experience;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ExperiencesRepositoryTest {

    private ExperiencesRepository experiencesRepository;

    @Before
    public void setup() {
        experiencesRepository = new ExperiencesRepository();
    }

    @Test
    public void shouldReturnByLocation() {
        String location = "PARIS";
        List<Experience> experiences = experiencesRepository.location(location).get();

        assertThat(experiences, is(not(empty())));
        experiences.forEach(exp -> assertThat(exp.getLocation(), is(location)));
    }

    @Test
    public void shouldReturnByAvailability() {
        int availability = 5;
        List<Experience> experiences = experiencesRepository.availability(availability).get();

        assertThat(experiences, is(not(empty())));
        experiences.forEach(exp -> assertThat(exp.getTickets(), is(greaterThanOrEqualTo(availability))));
    }

    @Test
    public void shouldReturnByAvailabilityAndLocation() {
        int availability = 3;
        String location = "MADRID";
        List<Experience> experiences = experiencesRepository
            .availability(availability)
            .location(location).get();

        assertThat(experiences, is(not(empty())));
        experiences.forEach(exp -> {
            assertThat(exp.getTickets(), is(greaterThanOrEqualTo(availability)));
            assertThat(exp.getLocation(), is(location));
        });
    }
}