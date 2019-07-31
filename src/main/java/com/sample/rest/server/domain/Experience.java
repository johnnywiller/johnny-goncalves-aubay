package com.sample.rest.server.domain;

import com.sample.rest.server.domain.valueobject.Money;
import com.sample.rest.server.domain.valueobject.PricedExperienceVO;

import java.time.LocalDate;

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
