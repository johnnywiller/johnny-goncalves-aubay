package com.sample.rest.server.service;

import com.sample.rest.server.core.TimeProvider;
import com.sample.rest.server.domain.BookStatus;
import com.sample.rest.server.domain.valueobject.BookExperienceVO;
import com.sample.rest.server.domain.valueobject.BookedExperienceVO;
import com.sample.rest.server.domain.valueobject.Money;
import com.sample.rest.server.domain.valueobject.PricedExperienceVO;
import com.sample.rest.server.repositories.BookingRepository;
import com.sample.rest.server.repositories.ExperiencesRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ExperienceServiceTest {

    private final String SUCCESS_MSG = BookStatus.SUCCESS.getMessage();
    private final String FAILURE_MSG = BookStatus.FAILURE.getMessage();
    private ExperienceService experienceService;
    private LocalDate currentDate;
    private List<PricedExperienceVO> experiences;

    @Before
    public void setup() {
        ExperiencesRepository experiencesRepository = new ExperiencesRepository();
        BookingRepository bookingRepository = new BookingRepository();
        experienceService = new ExperienceService(experiencesRepository, bookingRepository);

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
    public void shouldMultiplyPriceByTravelersWhenBook() {
        int travelers = 5;
        String location = "LONDON";
        PricedExperienceVO experience = experienceService.getExperiences(this.currentDate, travelers, location).get(0);

        BookExperienceVO bookExperienceManyTravelers = BookExperienceVO.of(
            experience.getCode(),
            experience.getDate(),
            travelers
        );
        BookExperienceVO bookExperienceOneTraveler = BookExperienceVO.of(
            experience.getCode(),
            experience.getDate(),
            1
        );

        BookedExperienceVO bookedExperienceManyTravelers = experienceService.bookExperience(bookExperienceManyTravelers);
        BookedExperienceVO bookedExperienceOneTraveler = experienceService.bookExperience(bookExperienceOneTraveler);

        Money priceForOne = bookedExperienceOneTraveler.getPrice();
        Money priceForMany = bookedExperienceManyTravelers.getPrice();
        BigDecimal expectedAmount = priceForOne.getAmount().multiply(BigDecimal.valueOf(travelers));

        Money expectedPrice = new Money(
            priceForOne.getCurrency(),
            expectedAmount
        );

        assertThat(priceForMany, is(expectedPrice));
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

    @Test
    public void shouldFailBookSpaceUnavailable() {
        int travelers = 1;
        String location = "LONDON";
        PricedExperienceVO experience = experienceService.getExperiences(this.currentDate, travelers, location).get(0);

        int greaterTravelers = experience.getAvailability() + 1;

        BookExperienceVO bookExperience = BookExperienceVO.of(
            experience.getCode(),
            experience.getDate(),
            greaterTravelers
        );

        BookedExperienceVO bookedExperienceVO = experienceService.bookExperience(bookExperience);

        assertThat(bookedExperienceVO.getStatus(), is(BookStatus.FAILURE));
        assertThat(bookedExperienceVO.getCode(), is(experience.getCode()));
    }

    @Test
    public void shouldBookLondonSpaceAvailable() {
        int travelers = 1;
        String location = "LONDON";
        PricedExperienceVO experience = experienceService.getExperiences(this.currentDate, travelers, location).get(0);

        BookExperienceVO bookExperience = BookExperienceVO.of(
            experience.getCode(),
            experience.getDate(),
            travelers
        );

        BookedExperienceVO bookedExperienceVO = experienceService.bookExperience(bookExperience);

        assertThat(bookedExperienceVO.getStatus(), is(BookStatus.SUCCESS));
        assertThat(bookedExperienceVO.getCode(), is(experience.getCode()));
        assertThat(bookedExperienceVO.getPrice(), is(experience.getPrice()));
    }
}