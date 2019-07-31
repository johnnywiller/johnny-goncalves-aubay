package com.sample.rest.server.domain;

import com.sample.rest.server.core.TimeProvider;
import com.sample.rest.server.domain.valueobject.BookExperienceVO;
import com.sample.rest.server.domain.valueobject.BookedExperienceVO;
import com.sample.rest.server.domain.valueobject.Money;
import com.sample.rest.server.domain.valueobject.PricedExperienceVO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Experience {

    private int id;
    private String location;
    private Integer tickets;
    private Money price;

    private Experience(int id, String location, Integer tickets, Money price) {
        this.id = id;
        this.location = location;
        this.tickets = tickets;
        this.price = price;
    }

    public static Experience of(int id, String location, int tickets, Money price) {
        return new Experience(id, location, tickets, price);
    }

    public BookedExperienceVO book(BookExperienceVO bookExperienceVO) {
        PricedExperienceVO pricedExperienceVO = priceForDate(bookExperienceVO.getDate(), bookExperienceVO.getTravelers());

        if (!isSpaceAvailable(bookExperienceVO))
            return invalidBooking(bookExperienceVO, pricedExperienceVO);

        decreaseAvailability(bookExperienceVO.getTravelers());

        return validBooking(bookExperienceVO, pricedExperienceVO);
    }

    public PricedExperienceVO priceForDate(LocalDate date, Integer travelers) {
        PriceProvider provider = PriceProvider.ofDate(date);

        return PricedExperienceVO.of(
            this.id,
            date,
            provider.getPrice(this.price, travelers),
            this.tickets
        );
    }

    private void decreaseAvailability(Integer travelers) {
        this.tickets -= travelers;
    }

    private boolean isSpaceAvailable(BookExperienceVO bookExperienceVO) {
        return (this.tickets >= bookExperienceVO.getTravelers());
    }

    private BookedExperienceVO validBooking(BookExperienceVO bookExperienceVO, PricedExperienceVO pricedExperienceVO) {
        return BookedExperienceVO.of(
            BookStatus.SUCCESS,
            bookExperienceVO.getCode(),
            bookExperienceVO.getDate().format(DateTimeFormatter.ofPattern(TimeProvider.DATE_FORMAT)),
            bookExperienceVO.getTravelers(),
            pricedExperienceVO.getPrice()
        );
    }

    private BookedExperienceVO invalidBooking(BookExperienceVO bookExperienceVO, PricedExperienceVO pricedExperienceVO) {
        return BookedExperienceVO.of(
            BookStatus.FAILURE,
            bookExperienceVO.getCode(),
            bookExperienceVO.getDate().format(DateTimeFormatter.ofPattern(TimeProvider.DATE_FORMAT)),
            bookExperienceVO.getTravelers(),
            pricedExperienceVO.getPrice()
        );
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public Integer getTickets() {
        return tickets;
    }

    public Money getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Experience{" +
            "id=" + id +
            ", location='" + location + '\'' +
            ", tickets=" + tickets +
            ", price=" + price +
            '}';
    }
}
