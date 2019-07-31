package com.sample.rest.server.application;

import com.sample.rest.server.core.TimeProvider;
import com.sample.rest.server.repositories.BookingRepository;
import com.sample.rest.server.repositories.ExperiencesRepository;
import com.sample.rest.server.repositories.LocationsRepository;
import com.sample.rest.server.service.ExperienceService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class Binder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(TimeProvider.class).to(TimeProvider.class);
        bind(LocationsRepository.class).to(LocationsRepository.class).in(Singleton.class);
        bind(BookingRepository.class).to(BookingRepository.class).in(Singleton.class);
        bind(ExperiencesRepository.class).to(ExperiencesRepository.class).in(Singleton.class);
        bind(ExperienceService.class).to(ExperienceService.class).in(Singleton.class);
    }
}
