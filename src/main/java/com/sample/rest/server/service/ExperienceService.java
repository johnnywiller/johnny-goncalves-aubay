package com.sample.rest.server.service;

import com.sample.rest.server.domain.BookingException;
import com.sample.rest.server.domain.Experience;
import com.sample.rest.server.domain.valueobject.BookExperienceVO;
import com.sample.rest.server.domain.valueobject.BookedExperienceVO;
import com.sample.rest.server.domain.valueobject.PricedExperienceVO;
import com.sample.rest.server.repositories.BookingRepository;
import com.sample.rest.server.repositories.ExperiencesRepository;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExperienceService {

    private ExperiencesRepository experiencesRepository;
    private BookingRepository bookingRepository;

    @Inject
    public ExperienceService(ExperiencesRepository experiencesRepository, BookingRepository bookingRepository) {
        this.experiencesRepository = experiencesRepository;
        this.bookingRepository = bookingRepository;
    }

    public BookedExperienceVO bookExperience(BookExperienceVO bookExperienceVO) throws BookingException {
        Experience experience = experiencesRepository.find(bookExperienceVO.getCode());
        BookedExperienceVO bookedExperienceVO = experience.book(bookExperienceVO);

        experiencesRepository.store(experience);
        bookingRepository.store(bookedExperienceVO);

        return bookedExperienceVO;
    }

    public List<PricedExperienceVO> getExperiences(LocalDate date, Integer travelers, String location) {
        List<Experience> experiences = experiencesRepository
            .location(location)
            .availability(travelers)
            .get();

        return experiences
            .stream()
            .map(experience -> experience.priceForDate(date, travelers))
            .collect(Collectors.toList());
    }
}
