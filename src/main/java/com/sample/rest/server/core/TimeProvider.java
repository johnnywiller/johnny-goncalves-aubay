package com.sample.rest.server.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeProvider {

    public static final String DATE_FORMAT = "yyyyMMdd";

    public long getTimeInMillis() {
        return System.currentTimeMillis();
    }

    public ZonedDateTime getCurrentDateTime() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }


}
