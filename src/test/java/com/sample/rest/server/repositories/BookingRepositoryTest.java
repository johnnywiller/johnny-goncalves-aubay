package com.sample.rest.server.repositories;

import com.sample.rest.server.core.TimeProvider;
import com.sample.rest.server.domain.BookStatus;
import com.sample.rest.server.domain.valueobject.BookedExperienceVO;
import com.sample.rest.server.domain.valueobject.Money;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

public class BookingRepositoryTest {

    private BookingRepository bookingRepository;

    @Before
    public void setup() {
        bookingRepository = new BookingRepository();
    }

    @Test
    public void shouldStoreBookedExperience() {
        int experienceId = 9890;
        int travelers = 2;
        String currentDate = new TimeProvider()
            .getCurrentDateTime()
            .format(DateTimeFormatter
                .ofPattern(TimeProvider.DATE_FORMAT));

        BookedExperienceVO bookedExperienceVO = BookedExperienceVO.of(
            BookStatus.SUCCESS,
            experienceId,
            currentDate,
            travelers,
            new Money("EUR", BigDecimal.valueOf(100.0))
        );

        bookingRepository.store(bookedExperienceVO);
        List<BookedExperienceVO> allBookings = bookingRepository.findAll();

        assertThat(allBookings, hasItem(bookedExperienceVO));
    }
}