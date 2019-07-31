package com.sample.rest.server.domain.valueobject;

import java.time.LocalDate;

public final class PricedExperienceVO {

    private final int code;
    private final LocalDate date;
    private final Money price;
    private final Integer availability;

    private PricedExperienceVO(int code, LocalDate date, Money price, Integer availability) {
        this.code = code;
        this.date = date;
        this.price = price;
        this.availability = availability;
    }

    public static PricedExperienceVO of(int code, LocalDate date, Money price, Integer availability) {
        return new PricedExperienceVO(code, date, price, availability);
    }

    public LocalDate getDate() {
        return date;
    }

    public int getCode() {
        return code;
    }

    public Money getPrice() {
        return price;
    }

    public Integer getAvailability() {
        return availability;
    }

    @Override
    public String toString() {
        return "PricedExperienceVO{" +
            "code=" + code +
            ", date=" + date +
            ", price=" + price +
            ", availability=" + availability +
            '}';
    }
}
