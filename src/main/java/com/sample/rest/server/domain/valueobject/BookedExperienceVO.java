package com.sample.rest.server.domain.valueobject;

import com.sample.rest.server.domain.BookStatus;

import java.util.Objects;

public final class BookedExperienceVO {

    private final BookStatus status;
    private final Integer code;
    private final String date;
    private final Integer travelers;
    private final Money price;

    private BookedExperienceVO(BookStatus status, Integer code, String date, Integer travelers, Money price) {
        this.status = status;
        this.code = code;
        this.date = date;
        this.travelers = travelers;
        this.price = price;
    }

    public static BookedExperienceVO of(BookStatus status, Integer code, String date, Integer travelers, Money price) {
        return new BookedExperienceVO(status, code, date, travelers, price);
    }

    public BookStatus getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public Integer getTravelers() {
        return travelers;
    }

    public Money getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookedExperienceVO that = (BookedExperienceVO) o;
        return status.equals(that.status) &&
            code.equals(that.code) &&
            date.equals(that.date) &&
            travelers.equals(that.travelers) &&
            price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, code, date, travelers, price);
    }

    @Override
    public String toString() {
        return "BookedExperienceVO{" +
            "status='" + status + '\'' +
            ", code=" + code +
            ", date='" + date + '\'' +
            ", travelers=" + travelers +
            ", price=" + price +
            '}';
    }
}