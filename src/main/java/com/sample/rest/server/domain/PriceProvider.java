package com.sample.rest.server.domain;

import com.sample.rest.server.core.TimeProvider;
import com.sample.rest.server.domain.valueobject.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.function.BiFunction;

import static java.time.temporal.ChronoUnit.DAYS;

public enum PriceProvider {

    BASE_80((price, travelers) -> price.getAmount().multiply(BigDecimal.valueOf(.8))
        .multiply(BigDecimal.valueOf(travelers))),

    BASE_100((price, travelers) -> price.getAmount().multiply(BigDecimal.valueOf(1))
        .multiply(BigDecimal.valueOf(travelers))),

    BASE_120((price, travelers) -> price.getAmount().multiply(BigDecimal.valueOf(1.2))
        .multiply(BigDecimal.valueOf(travelers))),

    BASE_150((price, travelers) -> price.getAmount().multiply(BigDecimal.valueOf(1.5))
        .multiply(BigDecimal.valueOf(travelers)));

    private BiFunction<Money, Integer, BigDecimal> priceFunction;

    PriceProvider(BiFunction<Money, Integer, BigDecimal> priceFunction) {
        this.priceFunction = priceFunction;
    }

    public static PriceProvider ofDate(LocalDate date) {

        LocalDate now = new TimeProvider().getCurrentDateTime().toLocalDate();

        long daysBetween = DAYS.between(now, date);

        if (daysBetween > 30) {
            return BASE_80;
        } else if (daysBetween >= 16) {
            return BASE_100;
        } else if (daysBetween >= 3) {
            return BASE_120;
        } else {
            return BASE_150;
        }
    }

    public Money getPrice(Money basePrice, Integer travelers) {
        return new Money(basePrice.getCurrency(), priceFunction.apply(basePrice, travelers)
            .setScale(2, RoundingMode.HALF_UP));
    }
}
