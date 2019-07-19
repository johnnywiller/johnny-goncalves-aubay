package com.sample.rest.server.application;

import com.sample.rest.server.core.TimeProvider;
import com.sample.rest.server.repositories.LocationsRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class Binder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(TimeProvider.class).to(TimeProvider.class);
        bind(LocationsRepository.class).to(LocationsRepository.class).in(Singleton.class);
    }

}
