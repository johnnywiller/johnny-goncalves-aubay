package com.sample.rest.server.service;

import com.sample.rest.server.domain.Experience;
import com.sample.rest.server.domain.valueobject.PricedExperienceVO;
import com.sample.rest.server.repositories.ExperiencesRepository;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExperienceService {

    private ExperiencesRepository experiencesRepository;

    @Inject
    public ExperienceService(ExperiencesRepository experiencesRepository) {
        this.experiencesRepository = experiencesRepository;
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
