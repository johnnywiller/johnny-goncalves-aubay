package com.sample.rest.server.repositories;

import java.util.List;

public class LocationsRepository extends BaseRepository {

    public List<String> list() {
        return getDataSource().withHandle(handle ->
                handle.createQuery("SELECT DISTINCT(location) FROM experiences")
                        .mapTo(String.class)
                        .list()
        );
    }

}
