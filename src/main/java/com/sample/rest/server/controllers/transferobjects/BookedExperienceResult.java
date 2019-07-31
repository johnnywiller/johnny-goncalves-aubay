package com.sample.rest.server.controllers.transferobjects;

import com.sample.rest.server.domain.valueobject.BookedExperienceVO;

import java.math.BigDecimal;

/**
 * We've created these Transfer Objects mainly because of the bad experience with JAXB Json.
 */
public final class BookedExperienceResult {

    private final String status;
    private final String message;
    private final Integer code;
    private final String date;
    private final Integer travelers;
    private final BigDecimal price;

    private BookedExperienceResult(String status, String message, Integer code, String date, Integer travelers, BigDecimal price) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.date = date;
        this.travelers = travelers;
        this.price = price;
    }

    public static BookedExperienceResult fromBookedExperienceVO(BookedExperienceVO bookedExperienceVO) {
        return of(
            bookedExperienceVO.getStatus().toString(),
            bookedExperienceVO.getStatus().getMessage(),
            bookedExperienceVO.getCode(),
            bookedExperienceVO.getDate(),
            bookedExperienceVO.getTravelers(),
            bookedExperienceVO.getPrice().getAmount()
        );
    }

    public static BookedExperienceResult of(String status, String message, Integer code, String date, Integer travelers, BigDecimal price) {
        return new BookedExperienceResult(status, message, code, date, travelers, price);
    }

    public String getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getDate() {
        return this.date;
    }

    public Integer getTravelers() {
        return this.travelers;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

}
