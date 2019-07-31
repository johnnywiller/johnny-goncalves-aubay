package com.sample.rest.server.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private final String currency;
    private final BigDecimal amount;

    public Money(String currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return currency.equals(money.currency) &&
            amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amount);
    }

    @Override
    public String toString() {
        return "Money{" +
            "currency='" + currency + '\'' +
            ", amount=" + amount +
            '}';
    }
}
