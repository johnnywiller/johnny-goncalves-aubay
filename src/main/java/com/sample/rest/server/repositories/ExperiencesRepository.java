package com.sample.rest.server.repositories;

import com.sample.rest.server.domain.BookingException;
import com.sample.rest.server.domain.Experience;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExperiencesRepository extends BaseRepository implements Repository<Experience, Integer> {

    private final String BASE_QUERY = "SELECT e.id, e.location, " +
        "               a.tickets, p.price, p.currency " +
        "           FROM experiences e " +
        "           JOIN availability a " +
        "               ON e.id = a.experience_id" +
        "           JOIN prices p" +
        "               ON e.id = p.experience_id ";


    // used to simply the concatenation of the first condition in the builder
    private final String DUMMY_WHERE = "WHERE 1 = 1 ";
    private String currentQuery;

    private HashMap<String, Object> whereConditions;

    public ExperiencesRepository() {
        this.reset();
    }

    @Override
    public Experience find(Integer id) {
        return getDataSource()
            .withHandle(handle ->
                handle.createQuery(currentQuery + "AND id = :id")
                    .bind("id", id)
                    .map(new ExperienceMapper())
                    .findFirst()
                    .orElseThrow(() -> new BookingException("Experience doesn't exist"))
            );
    }

    /**
     * Get all entities
     * This method is analog to calling {@link #get()} without any criteria first
     *
     * @return All stored entities
     */
    @Override
    public List<Experience> findAll() {
        this.reset();
        return get();
    }

    /**
     * TODO: for now the only update that matters is the last one, but is fine to add the persistence of all entities's fields
     *
     * @param experience Entity to store
     * @return The same Entity
     */
    public Experience store(Experience experience) {
        getDataSource().withHandle(handle -> {

            handle.createUpdate("UPDATE experiences SET location = :location WHERE id = :id")
                .bind("location", experience.getLocation())
                .bind("id", experience.getId())
                .execute();

            handle.createUpdate("UPDATE prices SET price = :price WHERE experience_id = :id")
                .bind("price", experience.getPrice().getAmount())
                .bind("id", experience.getId())
                .execute();

            return handle.createUpdate("UPDATE availability SET tickets = :tickets WHERE experience_id = :id")
                .bind("tickets", experience.getTickets())
                .bind("id", experience.getId())
                .execute();
        });
        return experience;
    }

    /**
     * Prepare this repository to return only entities with the desired availability or greater
     *
     * @param availability minimum availability requested
     * @return This repository, with availability condition included
     */
    public ExperiencesRepository availability(Integer availability) {
        if (Objects.isNull(availability))
            return this;

        this.currentQuery += "AND a.tickets >= :tickets ";
        this.whereConditions.put("tickets", availability);
        return this;
    }

    /**
     * Prepare this repository to return only entities with the desired location
     *
     * @param location minimum availability requested
     * @return This repository, with location condition included
     */
    public ExperiencesRepository location(String location) {
        if (Objects.isNull(location) || location.isBlank())
            return this;

        this.currentQuery += "AND e.location = :location ";
        this.whereConditions.put("location", location);
        return this;
    }

    /**
     * Get the entities that match the criteria specified by this repository.
     * <b>After this operation any criteria will be reset</b>
     * (i.e. any previous call to location or availability need to be done again)
     *
     * @return All entities that match the criteria specified by this repository.
     */
    public List<Experience> get() {
        List<Experience> experiences = getDataSource()
            .withHandle(handle ->
                handle.createQuery(currentQuery)
                    .bindMap(whereConditions.isEmpty() ? null : whereConditions)
                    .map(new ExperienceMapper())
                    .collect(Collectors.toList())
            );

        this.reset();
        return experiences;
    }

    private void reset() {
        this.whereConditions = new HashMap<>();
        this.currentQuery = BASE_QUERY + DUMMY_WHERE;
    }
}
