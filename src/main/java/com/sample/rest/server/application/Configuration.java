package com.sample.rest.server.application;

import org.glassfish.jersey.server.ResourceConfig;

public class Configuration extends ResourceConfig {

    public Configuration() {
        packages("com.sample.rest.server.controllers");
        register(new Binder());
    }

}
