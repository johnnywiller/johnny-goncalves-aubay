package com.sample.rest.server.repositories;

public class BookingRepository extends BaseRepository {

    /**
     * Bellow you'll find an example of how to write values to the database.
     * This is a mocked example only that you can use as guidance, nothing else.
     * TODO: Change this method signature, return types and query named parameters' values as you which for your solution
     */
    public int create() {
        return getDataSource().withHandle(handle ->
            handle.createUpdate("INSERT INTO bookings(experience_id, booking_date, travelers, price, currency) VALUES (:experience_id, :booking_date, :travelers, :price, :currency)")
                .bind("experience_id", 1234)
                .bind("booking_date", 20190312)
                .bind("travelers", 2)
                .bind("price", 25.00)
                .bind("currency", "EUR")
                .execute()
        );
    }

}
