package com.sample.rest.server.repositories;

import com.sample.rest.server.domain.Experience;
import com.sample.rest.server.domain.valueobject.Money;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExperienceMapper implements RowMapper<Experience> {

    @Override
    public Experience map(ResultSet rs, StatementContext ctx) throws SQLException {

        int id = rs.getInt("id");
        String location = rs.getString("location");
        int tickets = rs.getInt("tickets");
        BigDecimal price = rs.getBigDecimal("price");
        String currency = rs.getString("currency");

        return Experience.of(
            id,
            location,
            tickets,
            new Money(currency, price)
        );
    }
}
