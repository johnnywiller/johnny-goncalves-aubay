package com.sample.rest.server.service;

import com.sample.rest.server.core.TimeProvider;
import com.sample.rest.server.domain.valueobject.Money;
import com.sample.rest.server.domain.valueobject.PricedExperienceVO;
import com.sample.rest.server.repositories.ExperiencesRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ExperienceServiceTest {

    private final String SUCCESS_MSG = "Your booking is confirmed";
    private final String FAILURE_MSG = "Insufficient space";
    private ExperienceService experienceService;
    private LocalDate currentDate;
    private List<PricedExperienceVO> experiences;

    @Before
    public void setup() {
        ExperiencesRepository experiencesRepository = new ExperiencesRepository();
        experienceService = new ExperienceService(experiencesRepository);

        this.currentDate = new TimeProvider()
            .getCurrentDateTime()
            .toLocalDate();
    }

    @Test
    public void shouldReturnAvailableExperiences() {
        int requiredAvailability = 15;
        String location = "PARIS";

        List<PricedExperienceVO> experiences = experienceService.getExperiences(this.currentDate, requiredAvailability, location);

        assertThat(experiences, not(empty()));
        experiences.forEach(exp -> {
            assertThat(exp.getAvailability(), is(greaterThanOrEqualTo(requiredAvailability)));
        });
    }

    @Test
    public void shouldMultiplyPriceByTravelersWhenSearch() {
        int oneTraveler = 1;
        int manyTravelers = 2;
        String location = "LONDON";

        PricedExperienceVO experienceOneTraveler = experienceService.getExperiences(this.currentDate, oneTraveler, location).get(0);
        PricedExperienceVO experienceManyTraveler = experienceService.getExperiences(this.currentDate, manyTravelers, location).get(0);

        // ensure they are the same experience
        assertThat(experienceOneTraveler.getCode(), is(experienceManyTraveler.getCode()));

        Money priceForOne = experienceOneTraveler.getPrice();
        Money priceForMany = experienceManyTraveler.getPrice();
        BigDecimal expectedAmount = priceForOne.getAmount().multiply(BigDecimal.valueOf(manyTravelers));

        Money expectedPrice = new Money(
            priceForOne.getCurrency(),
            expectedAmount
        );

        assertThat(priceForMany, is(expectedPrice));
    }
}