package com.sample.rest.server.domain.valueobject;

import java.time.LocalDate;
import java.util.Objects;

public final class BookExperienceVO {

    private final Integer code;
    private final LocalDate date;
    private final Integer travelers;

    private BookExperienceVO(Integer code, LocalDate date, Integer travelers) {
        this.code = code;
        this.date = date;
        this.travelers = travelers;
    }

    public static BookExperienceVO of(Integer code, LocalDate date, Integer travelers) {
        return new BookExperienceVO(code, date, travelers);
    }

    public Integer getCode() {
        return code;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getTravelers() {
        return travelers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookExperienceVO that = (BookExperienceVO) o;
        return code.equals(that.code) &&
            date.equals(that.date) &&
            travelers.equals(that.travelers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, date, travelers);
    }
}
