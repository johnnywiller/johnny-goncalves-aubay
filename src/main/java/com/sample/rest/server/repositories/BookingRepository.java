package com.sample.rest.server.repositories;

import com.sample.rest.server.domain.BookStatus;
import com.sample.rest.server.domain.valueobject.BookedExperienceVO;
import com.sample.rest.server.domain.valueobject.Money;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO: We are using a Value Object in this Repository, but this isn't right according to DDD principles.
 * TODO: Should add an identity to BookedExperience (such as ID field on DB)
 */
public class BookingRepository extends BaseRepository implements Repository<BookedExperienceVO, Integer> {

    @Override
    public BookedExperienceVO find(Integer integer) {
        throw new UnsupportedOperationException("Query one book by ID is not supported yet");
    }

    @Override
    public List<BookedExperienceVO> findAll() {
        return getDataSource().withHandle(handle ->
            handle.createQuery("SELECT * FROM bookings")
                .map((rs, ctx) ->
                    BookedExperienceVO.of(
                        BookStatus.SUCCESS,
                        rs.getInt("experience_id"),
                        rs.getString("booking_date"),
                        rs.getInt("travelers"),
                        new Money("EUR", rs.getBigDecimal("price"))
                    )
                )
                .collect(Collectors.toList())
        );
    }

    @Override
    @Transaction
    public BookedExperienceVO store(BookedExperienceVO bookedExperienceVO) {
        int count = getDataSource().withHandle(handle ->
            handle.createUpdate("INSERT INTO bookings(experience_id, booking_date, travelers, price, currency) " +
                "VALUES (:experience_id, :booking_date, :travelers, :price, :currency)")
                .bind("experience_id", bookedExperienceVO.getCode())
                .bind("booking_date", bookedExperienceVO.getDate())
                .bind("travelers", bookedExperienceVO.getTravelers())
                .bind("price", bookedExperienceVO.getPrice().getAmount())
                .bind("currency", bookedExperienceVO.getPrice().getCurrency())
                .execute()
        );
        return bookedExperienceVO;
    }
}
