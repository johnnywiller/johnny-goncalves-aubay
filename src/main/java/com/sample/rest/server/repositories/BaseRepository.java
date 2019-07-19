package com.sample.rest.server.repositories;

import org.jdbi.v3.core.Jdbi;
import org.sqlite.SQLiteDataSource;

abstract class BaseRepository {

    private final Jdbi jdbi;

    BaseRepository() {
        final SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite::resource:rex.sqlite");
        jdbi = Jdbi.create(dataSource);
    }

    Jdbi getDataSource() {
        return jdbi;
    }

}
